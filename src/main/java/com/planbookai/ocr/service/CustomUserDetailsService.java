package com.planbookai.ocr.service;

import com.planbookai.ocr.model.User;
import com.planbookai.ocr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Tìm user trong Database (Đã thêm .orElse(null) để trị cái lỗi Optional)
        User user = userRepository.findByUsername(username).orElse(null);
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // 2. Ép quyền từ DB vào thẻ bài (Bản "Bao sân" chấp mọi thể loại check quyền)
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // Lấy quyền trong DB, nếu rỗng thì mặc định cho làm "TEACHER"
        String dbRole = (user.getRole() != null && !user.getRole().trim().isEmpty()) 
                        ? user.getRole().toUpperCase() 
                        : "TEACHER";

        // Cấp luôn 2 thẻ: 1 thẻ trần trụi và 1 thẻ có chữ ROLE_ đằng trước
        authorities.add(new SimpleGrantedAuthority(dbRole));         // Để pass qua hasAuthority()
        authorities.add(new SimpleGrantedAuthority("ROLE_" + dbRole)); // Để pass qua hasRole()
        System.out.println("🚀 [DEBUG] Đã cấp thẻ bài: [" + dbRole + "] và [ROLE_" + dbRole + "] cho user: " + username);

        // 3. Trả về đối tượng User của Spring Security kèm theo quyền
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities 
        );
    }
}