package com.wanchcoach.domain.drug.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFavoriteDrug is a Querydsl query type for FavoriteDrug
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFavoriteDrug extends EntityPathBase<FavoriteDrug> {

    private static final long serialVersionUID = -1035436333L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFavoriteDrug favoriteDrug = new QFavoriteDrug("favoriteDrug");

    public final com.wanchcoach.global.entity.QBaseEntity _super = new com.wanchcoach.global.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final QDrug drug;

    public final NumberPath<Long> favoriteId = createNumber("favoriteId", Long.class);

    public final NumberPath<Long> member = createNumber("member", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public QFavoriteDrug(String variable) {
        this(FavoriteDrug.class, forVariable(variable), INITS);
    }

    public QFavoriteDrug(Path<? extends FavoriteDrug> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFavoriteDrug(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFavoriteDrug(PathMetadata metadata, PathInits inits) {
        this(FavoriteDrug.class, metadata, inits);
    }

    public QFavoriteDrug(Class<? extends FavoriteDrug> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.drug = inits.isInitialized("drug") ? new QDrug(forProperty("drug"), inits.get("drug")) : null;
    }

}

