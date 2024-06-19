package com.wanchcoach.domain.drug.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDrugImage is a Querydsl query type for DrugImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDrugImage extends EntityPathBase<DrugImage> {

    private static final long serialVersionUID = 1674254724L;

    public static final QDrugImage drugImage = new QDrugImage("drugImage");

    public final com.wanchcoach.global.entity.QBaseEntity _super = new com.wanchcoach.global.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> drug_image_id = createNumber("drug_image_id", Long.class);

    public final StringPath filePath = createString("filePath");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final StringPath originFileName = createString("originFileName");

    public QDrugImage(String variable) {
        super(DrugImage.class, forVariable(variable));
    }

    public QDrugImage(Path<? extends DrugImage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDrugImage(PathMetadata metadata) {
        super(DrugImage.class, metadata);
    }

}

