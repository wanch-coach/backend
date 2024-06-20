package com.wanchcoach.domain.drug.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDrug is a Querydsl query type for Drug
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDrug extends EntityPathBase<Drug> {

    private static final long serialVersionUID = -1453205993L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDrug drug = new QDrug("drug");

    public final com.wanchcoach.global.entity.QBaseEntity _super = new com.wanchcoach.global.entity.QBaseEntity(this);

    public final StringPath bigPrdtImgUrl = createString("bigPrdtImgUrl");

    public final StringPath changeDate = createString("changeDate");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> drugId = createNumber("drugId", Long.class);

    public final QDrugImage drugImageId;

    public final StringPath eeDocData = createString("eeDocData");

    public final StringPath entpName = createString("entpName");

    public final StringPath entpSeq = createString("entpSeq");

    public final StringPath itemEngName = createString("itemEngName");

    public final StringPath itemIngrName = createString("itemIngrName");

    public final StringPath itemName = createString("itemName");

    public final StringPath itemPermitDate = createString("itemPermitDate");

    public final NumberPath<Long> itemSeq = createNumber("itemSeq", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final StringPath nbDocData = createString("nbDocData");

    public final StringPath prductType = createString("prductType");

    public final StringPath spcltyPblc = createString("spcltyPblc");

    public final StringPath storageMethod = createString("storageMethod");

    public final StringPath udDocData = createString("udDocData");

    public final StringPath validTerm = createString("validTerm");

    public QDrug(String variable) {
        this(Drug.class, forVariable(variable), INITS);
    }

    public QDrug(Path<? extends Drug> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDrug(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDrug(PathMetadata metadata, PathInits inits) {
        this(Drug.class, metadata, inits);
    }

    public QDrug(Class<? extends Drug> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.drugImageId = inits.isInitialized("drugImageId") ? new QDrugImage(forProperty("drugImageId")) : null;
    }

}

