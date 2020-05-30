package com.pody.contoller.impl;

import com.pody.contoller.UserController;
import com.pody.dto.requests.IdStringRequestDto;
import com.pody.dto.requests.StringRequestDto;
import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.dto.responses.IdResponseDto;
import com.pody.model.User;
import com.pody.service.managers.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@CrossOrigin(origins = {"*", "https://pody.ir" , "https://www.pody.ir"}, maxAge = 3600)
public class UserControllerImpl implements UserController {
    @Autowired
    private UserManager userManager;

    @Override
    public ResponseEntity read(@RequestBody TwoIDRequestDto dto) {
        return userManager.read(dto);
    }

    @Override
    public ResponseEntity update(@RequestBody User user, @PathVariable UUID id) {
        return userManager.update(user, id);
    }

    @Override
    public ResponseEntity delete(@PathVariable UUID id) {
        return userManager.delete(id);
    }

    @Override
    public ResponseEntity updateIsPremium(@PathVariable UUID id) {
        return userManager.isPremium(id);
    }

    @Override
    public ResponseEntity uploadProfileImage(@RequestParam("file") MultipartFile image, @PathVariable UUID id) {
        return userManager.uploadProfileImage(image, id);
    }

    @Override
    public ResponseEntity uploadChannelImage(@RequestParam("file") MultipartFile image, @PathVariable UUID id) {
        return userManager.uploadChannelImage(image, id);
    }

    @Override
    public ResponseEntity checkEmail(@RequestBody StringRequestDto dto) {
        return userManager.checkEmail(dto);
    }

    @Override
    public ResponseEntity checkUsername(@RequestBody StringRequestDto dto) {
        return userManager.checkUsername(dto);
    }

    @Override
    public ResponseEntity resetPassword(@RequestBody IdStringRequestDto dto) {
        return userManager.resetPassword(dto);
    }

    @Override
    public ResponseEntity findUserForResetPassword(@RequestBody StringRequestDto dto) {
        return userManager.findUserForResetPassword(dto);
    }

    @Override
    public ResponseEntity listChannels(@RequestBody IdResponseDto dto) {
        return userManager.listChannels(dto);
    }

    @Override
    public ResponseEntity updateIsChannel(IdResponseDto dto) {
        return userManager.updateIsChannel(dto);
    }

    @Override
    public ResponseEntity userListSubscriptions(@PathVariable("id") UUID id) {
        return userManager.userListSubscriptions(id);
    }

}
