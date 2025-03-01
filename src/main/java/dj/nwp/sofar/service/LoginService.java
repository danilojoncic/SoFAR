package dj.nwp.sofar.service;

import dj.nwp.sofar.config.JWTUtil;
import dj.nwp.sofar.dto.LoginRequest;
import dj.nwp.sofar.dto.LoginResponse;
import dj.nwp.sofar.dto.Message;
import dj.nwp.sofar.dto.ServiceResponse;
import dj.nwp.sofar.model.SUser;
import dj.nwp.sofar.repository.UserRepository;
import dj.nwp.sofar.service.abstraction.AuthAbs;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements AuthAbs {
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ServiceResponse login(LoginRequest atp) {
        if (userRepository.existsByEmail(atp.email())) {
            SUser st = userRepository.findByEmail(atp.email()).orElse(null);
            if (st != null && passwordEncoder.matches(atp.password(),st.getPassword())) {
                String jwt = jwtUtil.generateToken(st);
                LoginResponse loginResponse = new LoginResponse(jwt);
                return new ServiceResponse(200,loginResponse);
            }else{
                return new ServiceResponse(401,new Message("Invalid email or password"));
            }
        }
        return null;
    }
}
