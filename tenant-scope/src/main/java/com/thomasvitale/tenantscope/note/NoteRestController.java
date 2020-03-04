package com.thomasvitale.tenantscope.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteRestController {
    private final NoteService noteService;

    @Autowired
    public NoteRestController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public List<NoteDTO> getNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping("tenant")
    public String getTenantIdentifier() {
        return noteService.tenantIdentifier();
    }
}
