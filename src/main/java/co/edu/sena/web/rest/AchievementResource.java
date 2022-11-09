package co.edu.sena.web.rest;

import co.edu.sena.repository.AchievementRepository;
import co.edu.sena.service.AchievementService;
import co.edu.sena.service.dto.AchievementDTO;
import co.edu.sena.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link co.edu.sena.domain.Achievement}.
 */
@RestController
@RequestMapping("/api")
public class AchievementResource {

    private final Logger log = LoggerFactory.getLogger(AchievementResource.class);

    private static final String ENTITY_NAME = "achievement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AchievementService achievementService;

    private final AchievementRepository achievementRepository;

    public AchievementResource(AchievementService achievementService, AchievementRepository achievementRepository) {
        this.achievementService = achievementService;
        this.achievementRepository = achievementRepository;
    }

    /**
     * {@code POST  /achievements} : Create a new achievement.
     *
     * @param achievementDTO the achievementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new achievementDTO, or with status {@code 400 (Bad Request)} if the achievement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/achievements")
    public ResponseEntity<AchievementDTO> createAchievement(@Valid @RequestBody AchievementDTO achievementDTO) throws URISyntaxException {
        log.debug("REST request to save Achievement : {}", achievementDTO);
        if (achievementDTO.getId() != null) {
            throw new BadRequestAlertException("A new achievement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AchievementDTO result = achievementService.save(achievementDTO);
        return ResponseEntity
            .created(new URI("/api/achievements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /achievements/:id} : Updates an existing achievement.
     *
     * @param id the id of the achievementDTO to save.
     * @param achievementDTO the achievementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated achievementDTO,
     * or with status {@code 400 (Bad Request)} if the achievementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the achievementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/achievements/{id}")
    public ResponseEntity<AchievementDTO> updateAchievement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AchievementDTO achievementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Achievement : {}, {}", id, achievementDTO);
        if (achievementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, achievementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!achievementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AchievementDTO result = achievementService.update(achievementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, achievementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /achievements/:id} : Partial updates given fields of an existing achievement, field will ignore if it is null
     *
     * @param id the id of the achievementDTO to save.
     * @param achievementDTO the achievementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated achievementDTO,
     * or with status {@code 400 (Bad Request)} if the achievementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the achievementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the achievementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/achievements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AchievementDTO> partialUpdateAchievement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AchievementDTO achievementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Achievement partially : {}, {}", id, achievementDTO);
        if (achievementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, achievementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!achievementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AchievementDTO> result = achievementService.partialUpdate(achievementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, achievementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /achievements} : get all the achievements.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of achievements in body.
     */
    @GetMapping("/achievements")
    public ResponseEntity<List<AchievementDTO>> getAllAchievements(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Achievements");
        Page<AchievementDTO> page;
        if (eagerload) {
            page = achievementService.findAllWithEagerRelationships(pageable);
        } else {
            page = achievementService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /achievements/:id} : get the "id" achievement.
     *
     * @param id the id of the achievementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the achievementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/achievements/{id}")
    public ResponseEntity<AchievementDTO> getAchievement(@PathVariable Long id) {
        log.debug("REST request to get Achievement : {}", id);
        Optional<AchievementDTO> achievementDTO = achievementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(achievementDTO);
    }

    /**
     * {@code DELETE  /achievements/:id} : delete the "id" achievement.
     *
     * @param id the id of the achievementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/achievements/{id}")
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long id) {
        log.debug("REST request to delete Achievement : {}", id);
        achievementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
