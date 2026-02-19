package com.ged.infrastructure.persistence.mapper;

import com.ged.domain.model.User;
import com.ged.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {
    public User toDomain(UserEntity entity) {
        if (entity == null) return null;
        
        return User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .role(entity.getRole())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public UserEntity toEntity(User user) {
        if (user == null) return null;
        
        return UserEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
