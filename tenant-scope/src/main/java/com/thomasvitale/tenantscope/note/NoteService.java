package com.thomasvitale.tenantscope.note;

import com.thomasvitale.tenantscope.context.TenantContextHolder;
import com.thomasvitale.tenantscope.context.TenantScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@TenantScope
public class NoteService {
    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<NoteDTO> getAllNotes() {
        List<NoteDTO> noteDTOS = new ArrayList<>();
        noteRepository.findAll().forEach(organization ->
                noteDTOS.add(new NoteDTO(organization.getTitle(), organization.getContent()))
        );
        return noteDTOS;
    }

    public String tenantIdentifier() {
        return TenantContextHolder.getTenantIdentifier();
    }
}
