package com.wanchcoach.domain.drug.entity;

import com.wanchcoach.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

@NoArgsConstructor
@Getter
@Entity
@SuperBuilder
@DynamicInsert
@ToString
public class DrugImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long drug_image_id;

    @JoinColumn(name="origin_file_name")
    private String originFileName;

    @JoinColumn(name="file_path")
    private String filePath;
}
