package com.openclassroom.chatop.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email", name = "USERS_index"))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name="created_at")
	@CreationTimestamp
	private LocalDateTime createdAt;
	 
	@Column(name="updated_at")
	@UpdateTimestamp
	private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
	 private List<Rental> rentals;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
 
    @Override
    public String getPassword() {
        return this.password;
    }
 
    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
 
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
 
     @Override
     public boolean isEnabled() {
         return true;
     }

}