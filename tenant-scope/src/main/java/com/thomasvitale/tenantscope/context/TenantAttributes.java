package com.thomasvitale.tenantscope.context;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Object to access attribute objects associated with a tenant request.
 * Supports access to tenant-scoped attributes.
 *
 * Offers a tenant request completion mechanism for tenant-specific destruction
 * callbacks.
 */
public class TenantAttributes {

    private final String tenantIdentifier;
    private final Map<String, Object> tenantScope;
    private final Map<String, Runnable> tenantRequestDestructionCallbacks;

    private volatile boolean tenantActive = true;

    /**
     * Create a new TenantAttributes instance for the given tenant.
     * @param tenantIdentifier current tenant
     */
    public TenantAttributes(String tenantIdentifier) {
        Assert.notNull(tenantIdentifier, "Tenant identifier must not be null.");
        this.tenantIdentifier = tenantIdentifier;
        this.tenantScope = new HashMap<>();
        this.tenantRequestDestructionCallbacks = new ConcurrentHashMap<>(8);
    }

    /**
     * Exposes the native tenant identifier that we're wrapping.
     */
    public final String getTenant() {
        return this.tenantIdentifier;
    }

    /**
     * Return the value for the scoped attribute of the given name, if any.
     * @param name the name of the attribute
     * @return the current attribute value, or {@code null} if not found
     */
    @Nullable
    public Object getAttribute(String name) {
        if (!isTenantActive()) {
            throw new IllegalStateException("Cannot ask for tenant attribute - tenant is not active anymore!");
        }
        return this.tenantScope.get(name);
    }

    /**
     * Set the value for the scoped attribute of the given name,
     * replacing an existing value (if any).
     * @param name the name of the attribute
     * @param value the value for the attribute
     */
    public void setAttribute(String name, Object value) {
        if (!isTenantActive()) {
            throw new IllegalStateException("Cannot set tenant attribute - tenant is not active anymore!");
        }
        this.tenantScope.put(name, value);
    }

    /**
     * Remove the scoped attribute of the given name, if it exists.
     * <p>Note that an implementation should also remove a registered destruction
     * callback for the specified attribute, if any. It does, however, <i>not</i>
     * need to <i>execute</i> a registered destruction callback in this case,
     * since the object will be destroyed by the caller (if appropriate).
     * @param name the name of the attribute
     */
    public void removeAttribute(String name) {
        if (isTenantActive()) {
            removeTenantDestructionCallback(name);
            this.tenantScope.remove(name);
        }
    }

    /**
     * Register a callback to be executed on destruction of the
     * specified attribute in the given scope.
     * <p>Implementations should do their best to execute the callback
     * at the appropriate time: that is, at request completion or session
     * termination, respectively. If such a callback is not supported by the
     * underlying runtime environment, the callback <i>must be ignored</i>
     * and a corresponding warning should be logged.
     * <p>Note that 'destruction' usually corresponds to destruction of the
     * entire scope, not to the individual attribute having been explicitly
     * removed by the application. If an attribute gets removed via this
     * facade's {@link #removeAttribute(String)} method, any registered
     * destruction callback should be disabled as well, assuming that the
     * removed object will be reused or manually destroyed.
     * <p><b>NOTE:</b> Callback objects should generally be serializable if
     * they are being registered for a session scope. Otherwise the callback
     * (or even the entire session) might not survive web app restarts.
     * @param name the name of the attribute to register the callback for
     * @param callback the destruction callback to be executed
     */
    public void registerDestructionCallback(String name, Runnable callback) {
        registerTenantDestructionCallback(name, callback);
    }

    /**
     * Signal that the tenant request has been completed.
     * <p>Executes all tenant request destruction callbacks.
     */
    public void tenantRequestCompleted() {
        executeTenantRequestDestructionCallbacks();
        this.tenantActive = false;
    }

    /**
     * Determine whether the original tenant request is still active.
     * @see #tenantRequestCompleted()
     */
    protected final boolean isTenantActive() {
        return this.tenantActive;
    }

    /**
     * Register the given callback as to be executed after tenant request completion.
     * @param name the name of the attribute to register the callback for
     * @param callback the callback to be executed for destruction
     */
    protected final void registerTenantDestructionCallback(String name, Runnable callback) {
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(callback, "Callback must not be null");
        synchronized (this.tenantRequestDestructionCallbacks) {
            this.tenantRequestDestructionCallbacks.put(name, callback);
        }
    }

    /**
     * Remove the tenant request destruction callback for the specified attribute, if any.
     * @param name the name of the attribute to remove the callback for
     */
    protected final void removeTenantDestructionCallback(String name) {
        Assert.notNull(name, "Name must not be null");
        synchronized (this.tenantRequestDestructionCallbacks) {
            this.tenantRequestDestructionCallbacks.remove(name);
        }
    }

    /**
     * Execute all callbacks that have been registered for execution
     * after tenant request completion.
     */
    private void executeTenantRequestDestructionCallbacks() {
        synchronized (this.tenantRequestDestructionCallbacks) {
            for (Runnable runnable : this.tenantRequestDestructionCallbacks.values()) {
                runnable.run();
            }
            this.tenantRequestDestructionCallbacks.clear();
        }
    }
}
