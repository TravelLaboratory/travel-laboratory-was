package site.travellaboratory.be.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.travellaboratory.be.controller.dto.UserProfileUpdateRequest;
import site.travellaboratory.be.domain.user.User;
import site.travellaboratory.be.domain.user.UserRepository;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = User.of(1L, "유선준");
        User user1 = User.of(2L, "전인표");
        userRepository.save(user);
        userRepository.save(user1);
    }

    @Test
    void findByProfile() {
        //given
        userService.findByUserProfile(1L);
        //when

        //then
    }

    @Test
    void asd() {
        //given
        userService.updateProfile(new UserProfileUpdateRequest("준",  "안녕하세요"), 1L);
        //when

        //then
    }
}
