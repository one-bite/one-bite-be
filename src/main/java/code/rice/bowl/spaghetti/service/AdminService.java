package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    /**
     * 요청한 계정이 Admin 계정인지 확인.
     *
     * @param email 요청한 계정 이메일.
     * @return True : admin O , False : admin X
     */
    public boolean isAdmin(String email) {
        return adminRepository.findByEmail(email).isPresent();
    }

}
