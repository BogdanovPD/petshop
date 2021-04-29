package com.test.petshop.model;

import com.test.petshop.model.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Pet {
    @Id
    @GeneratedValue
    protected Integer id;
    @Column(nullable = false)
    protected String name;
    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinColumn(name = "CATEGORY_ID")
    protected Category category;
    @Enumerated(EnumType.STRING)
    protected Status status;
    @ElementCollection
    @CollectionTable(
            name="PET_PHOTO_URL",
            joinColumns=@JoinColumn(name="PET_ID")
    )
    @Column(name="PHOTO_URL", nullable = false)
    private List<String> photoUrls;
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "PET_TAG",
            joinColumns = @JoinColumn(name = "PET_ID"),
            inverseJoinColumns = @JoinColumn(name = "TAG_ID")
    )
    private Set<Tag> tags = new HashSet<>();

}
