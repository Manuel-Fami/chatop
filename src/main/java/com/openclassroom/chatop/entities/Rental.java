package com.openclassroom.chatop.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
// import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rentals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rental {
    @Id
    // Auto-incrémentation
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Pas de valeur à null
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal surface;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String picture;

    // Lob permet d'avoir une string plus longue que 255 caractères enregistré
    // @Lob
    @Column(nullable = false, length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name="created_at")
	@CreationTimestamp
	private LocalDateTime createdAt;
	 
	@Column(name="updated_at")
	@UpdateTimestamp
	private LocalDateTime updatedAt;

}
