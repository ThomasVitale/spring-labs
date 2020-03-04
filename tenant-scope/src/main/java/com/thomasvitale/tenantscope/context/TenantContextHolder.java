package com.thomasvitale.tenantscope.context;

import org.springframework.core.NamedThreadLocal;
import org.springframework.lang.Nullable;

/**
 * Holder class to expose the tenant request in the form of a thread-bound
 * {@link TenantAttributes} object. The tenant request will be inherited
 * by any child threads spawned by the current thread if the
 * {@code inheritable} flag is set to {@code true}.
 *
 * <p>Use {@link TenantContextListener} or
 * {@link TenantContextFilter} to expose
 * the current tenant request. Note that
 * {@link org.springframework.web.servlet.DispatcherServlet} already
 * expose the current tenant request by default.
 *
 * @see TenantContextListener
 * @see TenantContextFilter
 * @see org.springframework.web.servlet.DispatcherServlet
 */
public class TenantContextHolder {

    private static final ThreadLocal<TenantAttributes> tenantAttributesHolder =
            new NamedThreadLocal<>("Tenant attributes");

    private TenantContextHolder() {
        throw new AssertionError("This class is not instantiable.");
    }

    /**
     * Reset the TenantAttributes for the current thread.
     */
    public static void resetTenantAttributes() {
        tenantAttributesHolder.remove();
    }

    /**
     * Bind the given TenantAttributes to the current thread.
     *
     * @param attributes the TenantAttributes to expose
     */
    public static void setTenantAttributes(@Nullable TenantAttributes attributes) {
        if (attributes == null) {
            resetTenantAttributes();
        } else {
            tenantAttributesHolder.set(attributes);
        }
    }

    /**
     * Return the TenantAttributes currently bound to the thread.
     *
     * @return the TenantAttributes currently bound to the thread,
     * or {@code null} if none bound
     */
    @Nullable
    public static TenantAttributes getTenantAttributes() {
        return tenantAttributesHolder.get();
    }

    public static String getTenantIdentifier() {
        TenantAttributes tenantAttributes = tenantAttributesHolder.get();
        return tenantAttributes != null ? tenantAttributes.getTenant() : null;
    }

    /**
     * Return the TenantAttributes currently bound to the thread.
     * <p>Exposes the previously bound TenantAttributes instance, if any.
     *
     * @return the TenantAttributes currently bound to the thread
     * @throws IllegalStateException if no TenantAttributes object
     * is bound to the current thread
     * @see #setTenantAttributes
     */
    public static TenantAttributes currentTenantAttributes() {
        TenantAttributes attributes = getTenantAttributes();
        if (attributes == null) {
            throw new IllegalStateException("No thread-bound tenant request found: " +
                    "Are you referring to tenant attributes outside of an actual web request, " +
                    "or processing a tenant request outside of the originally receiving thread? " +
                    "If you are actually operating within a tenant request and still receive this message, " +
                    "your code is probably running outside of DispatcherServlet: " +
                    "In this case, use TenantContextListener or TenantContextFilter to expose the current tenant request.");
        }
        return attributes;
    }
}
