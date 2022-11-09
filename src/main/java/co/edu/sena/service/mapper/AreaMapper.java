package co.edu.sena.service.mapper;

import co.edu.sena.domain.Area;
import co.edu.sena.domain.Level;
import co.edu.sena.service.dto.AreaDTO;
import co.edu.sena.service.dto.LevelDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Area} and its DTO {@link AreaDTO}.
 */
@Mapper(componentModel = "spring")
public interface AreaMapper extends EntityMapper<AreaDTO, Area> {
    @Mapping(target = "levels", source = "levels", qualifiedByName = "levelIdSet")
    AreaDTO toDto(Area s);

    @Mapping(target = "removeLevel", ignore = true)
    Area toEntity(AreaDTO areaDTO);

    @Named("levelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LevelDTO toDtoLevelId(Level level);

    @Named("levelIdSet")
    default Set<LevelDTO> toDtoLevelIdSet(Set<Level> level) {
        return level.stream().map(this::toDtoLevelId).collect(Collectors.toSet());
    }
}
