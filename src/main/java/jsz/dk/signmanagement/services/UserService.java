package jsz.dk.signmanagement.services;

import jsz.dk.signmanagement.common.entity.CustomException;
import jsz.dk.signmanagement.entity.*;
import jsz.dk.signmanagement.entity.dto.GoogleDTO;
import jsz.dk.signmanagement.entity.dto.UserDTO;
import jsz.dk.signmanagement.entity.vo.GoogleSecretVO;
import jsz.dk.signmanagement.entity.vo.UserLoginVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.services
 * @ClassName: UserService
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/02 18:12
 */
public interface UserService {
    User registry(UserDTO dto) throws CustomException;

    GoogleSecretVO generateGoogleSecret(User user);

    UserLoginVO login(UserDTO dto) throws CustomException;

    void genQrCode(String secretQrCode, HttpServletResponse response) throws CustomException;

    void bindGoogle(GoogleDTO dto, User user, HttpServletRequest request) throws CustomException;

    void googleLogin(Long code, User user, HttpServletRequest request) throws CustomException;
}
