package site.travellaboratory.be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.travellaboratory.be.controller.dto.user.ProfileImgUpdateRequest;
import site.travellaboratory.be.controller.dto.user.ProfileImgUpdateResponse;
import site.travellaboratory.be.controller.dto.user.UserProfileResponse;
import site.travellaboratory.be.controller.dto.user.UserProfileUpdateRequest;
import site.travellaboratory.be.controller.dto.user.UserProfileUpdateResponse;
import site.travellaboratory.be.domain.user.User;
import site.travellaboratory.be.domain.user.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserProfileResponse findByUserProfile(final Long userId) {
        final User user = userRepository.getById(userId);
        return UserProfileResponse.from(user);
    }

    public UserProfileUpdateResponse updateProfile(
            final UserProfileUpdateRequest userProfileUpdateRequest,
            final Long userId
    ) {
        final User user = userRepository.getById(userId);
        final User updatedUser = user.update(userProfileUpdateRequest.nickname(), userProfileUpdateRequest.introduce());

        userRepository.save(updatedUser);

        final UserProfileUpdateResponse userProfileUpdateResponse = new UserProfileUpdateResponse(
                userProfileUpdateRequest.nickname(), userProfileUpdateRequest.introduce());
        return userProfileUpdateResponse;
    } // 프론트에서 유저네임 같은 경우에는 못 바꾸게 그대로 값 넘어오게 설정해야함 그때 맞춰서 파라미터 바꾸기.

    public ProfileImgUpdateResponse updateProfileImage(
            final ProfileImgUpdateRequest profileImgUpdateRequest,
            final Long userId
    ) {
        final User user = userRepository.getById(userId);
        final User updatedProfileImg = user.updateProfileImg(profileImgUpdateRequest.profileImgUrl());

        userRepository.save(updatedProfileImg);

        return new ProfileImgUpdateResponse(
                profileImgUpdateRequest.profileImgUrl());
    }
}
