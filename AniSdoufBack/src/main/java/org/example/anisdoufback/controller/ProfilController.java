package org.example.anisdoufback.controller;

import lombok.RequiredArgsConstructor;
import org.example.anisdoufback.dto.AvatarRequest;
import org.example.anisdoufback.dto.UtilisateurResponse;
import org.example.anisdoufback.service.UtilisateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profil")
@RequiredArgsConstructor
public class ProfilController {
    private final UtilisateurService utilisateurService;
    
    @GetMapping
    public ResponseEntity<?> getMonProfil(){
        try {
            String mail = SecurityContextHolder.getContext().getAuthentication().getName();
            UtilisateurResponse utilisateurResponse = utilisateurService.getMonProfil(mail);
            return ResponseEntity.ok(utilisateurResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur",e.getMessage()));
        }
    }

    @PutMapping("/avatar")
    public ResponseEntity<?> updateAvatar(@RequestBody AvatarRequest request){
        try {
            String mail = SecurityContextHolder.getContext().getAuthentication().getName();
            UtilisateurResponse response = utilisateurService.updateAvatar(mail, request.getAvatar());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erreur", e.getMessage()));
        }
    }
}
