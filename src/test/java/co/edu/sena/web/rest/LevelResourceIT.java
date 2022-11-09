package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.Level;
import co.edu.sena.repository.LevelRepository;
import co.edu.sena.service.LevelService;
import co.edu.sena.service.dto.LevelDTO;
import co.edu.sena.service.mapper.LevelMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LevelResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LevelResourceIT {

    private static final String DEFAULT_NAME_CURSE = "AAAAAAAAAA";
    private static final String UPDATED_NAME_CURSE = "BBBBBBBBBB";

    private static final String DEFAULT_COURSE_ACRONYM = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_ACRONYM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/levels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LevelRepository levelRepository;

    @Mock
    private LevelRepository levelRepositoryMock;

    @Autowired
    private LevelMapper levelMapper;

    @Mock
    private LevelService levelServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLevelMockMvc;

    private Level level;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Level createEntity(EntityManager em) {
        Level level = new Level().nameCurse(DEFAULT_NAME_CURSE).courseAcronym(DEFAULT_COURSE_ACRONYM);
        return level;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Level createUpdatedEntity(EntityManager em) {
        Level level = new Level().nameCurse(UPDATED_NAME_CURSE).courseAcronym(UPDATED_COURSE_ACRONYM);
        return level;
    }

    @BeforeEach
    public void initTest() {
        level = createEntity(em);
    }

    @Test
    @Transactional
    void createLevel() throws Exception {
        int databaseSizeBeforeCreate = levelRepository.findAll().size();
        // Create the Level
        LevelDTO levelDTO = levelMapper.toDto(level);
        restLevelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(levelDTO)))
            .andExpect(status().isCreated());

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeCreate + 1);
        Level testLevel = levelList.get(levelList.size() - 1);
        assertThat(testLevel.getNameCurse()).isEqualTo(DEFAULT_NAME_CURSE);
        assertThat(testLevel.getCourseAcronym()).isEqualTo(DEFAULT_COURSE_ACRONYM);
    }

    @Test
    @Transactional
    void createLevelWithExistingId() throws Exception {
        // Create the Level with an existing ID
        level.setId(1L);
        LevelDTO levelDTO = levelMapper.toDto(level);

        int databaseSizeBeforeCreate = levelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLevelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(levelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameCurseIsRequired() throws Exception {
        int databaseSizeBeforeTest = levelRepository.findAll().size();
        // set the field null
        level.setNameCurse(null);

        // Create the Level, which fails.
        LevelDTO levelDTO = levelMapper.toDto(level);

        restLevelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(levelDTO)))
            .andExpect(status().isBadRequest());

        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCourseAcronymIsRequired() throws Exception {
        int databaseSizeBeforeTest = levelRepository.findAll().size();
        // set the field null
        level.setCourseAcronym(null);

        // Create the Level, which fails.
        LevelDTO levelDTO = levelMapper.toDto(level);

        restLevelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(levelDTO)))
            .andExpect(status().isBadRequest());

        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLevels() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);

        // Get all the levelList
        restLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(level.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameCurse").value(hasItem(DEFAULT_NAME_CURSE)))
            .andExpect(jsonPath("$.[*].courseAcronym").value(hasItem(DEFAULT_COURSE_ACRONYM)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLevelsWithEagerRelationshipsIsEnabled() throws Exception {
        when(levelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLevelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(levelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLevelsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(levelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLevelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(levelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getLevel() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);

        // Get the level
        restLevelMockMvc
            .perform(get(ENTITY_API_URL_ID, level.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(level.getId().intValue()))
            .andExpect(jsonPath("$.nameCurse").value(DEFAULT_NAME_CURSE))
            .andExpect(jsonPath("$.courseAcronym").value(DEFAULT_COURSE_ACRONYM));
    }

    @Test
    @Transactional
    void getNonExistingLevel() throws Exception {
        // Get the level
        restLevelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLevel() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);

        int databaseSizeBeforeUpdate = levelRepository.findAll().size();

        // Update the level
        Level updatedLevel = levelRepository.findById(level.getId()).get();
        // Disconnect from session so that the updates on updatedLevel are not directly saved in db
        em.detach(updatedLevel);
        updatedLevel.nameCurse(UPDATED_NAME_CURSE).courseAcronym(UPDATED_COURSE_ACRONYM);
        LevelDTO levelDTO = levelMapper.toDto(updatedLevel);

        restLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, levelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(levelDTO))
            )
            .andExpect(status().isOk());

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate);
        Level testLevel = levelList.get(levelList.size() - 1);
        assertThat(testLevel.getNameCurse()).isEqualTo(UPDATED_NAME_CURSE);
        assertThat(testLevel.getCourseAcronym()).isEqualTo(UPDATED_COURSE_ACRONYM);
    }

    @Test
    @Transactional
    void putNonExistingLevel() throws Exception {
        int databaseSizeBeforeUpdate = levelRepository.findAll().size();
        level.setId(count.incrementAndGet());

        // Create the Level
        LevelDTO levelDTO = levelMapper.toDto(level);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, levelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(levelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLevel() throws Exception {
        int databaseSizeBeforeUpdate = levelRepository.findAll().size();
        level.setId(count.incrementAndGet());

        // Create the Level
        LevelDTO levelDTO = levelMapper.toDto(level);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(levelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLevel() throws Exception {
        int databaseSizeBeforeUpdate = levelRepository.findAll().size();
        level.setId(count.incrementAndGet());

        // Create the Level
        LevelDTO levelDTO = levelMapper.toDto(level);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLevelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(levelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLevelWithPatch() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);

        int databaseSizeBeforeUpdate = levelRepository.findAll().size();

        // Update the level using partial update
        Level partialUpdatedLevel = new Level();
        partialUpdatedLevel.setId(level.getId());

        restLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLevel))
            )
            .andExpect(status().isOk());

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate);
        Level testLevel = levelList.get(levelList.size() - 1);
        assertThat(testLevel.getNameCurse()).isEqualTo(DEFAULT_NAME_CURSE);
        assertThat(testLevel.getCourseAcronym()).isEqualTo(DEFAULT_COURSE_ACRONYM);
    }

    @Test
    @Transactional
    void fullUpdateLevelWithPatch() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);

        int databaseSizeBeforeUpdate = levelRepository.findAll().size();

        // Update the level using partial update
        Level partialUpdatedLevel = new Level();
        partialUpdatedLevel.setId(level.getId());

        partialUpdatedLevel.nameCurse(UPDATED_NAME_CURSE).courseAcronym(UPDATED_COURSE_ACRONYM);

        restLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLevel))
            )
            .andExpect(status().isOk());

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate);
        Level testLevel = levelList.get(levelList.size() - 1);
        assertThat(testLevel.getNameCurse()).isEqualTo(UPDATED_NAME_CURSE);
        assertThat(testLevel.getCourseAcronym()).isEqualTo(UPDATED_COURSE_ACRONYM);
    }

    @Test
    @Transactional
    void patchNonExistingLevel() throws Exception {
        int databaseSizeBeforeUpdate = levelRepository.findAll().size();
        level.setId(count.incrementAndGet());

        // Create the Level
        LevelDTO levelDTO = levelMapper.toDto(level);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, levelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(levelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLevel() throws Exception {
        int databaseSizeBeforeUpdate = levelRepository.findAll().size();
        level.setId(count.incrementAndGet());

        // Create the Level
        LevelDTO levelDTO = levelMapper.toDto(level);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(levelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLevel() throws Exception {
        int databaseSizeBeforeUpdate = levelRepository.findAll().size();
        level.setId(count.incrementAndGet());

        // Create the Level
        LevelDTO levelDTO = levelMapper.toDto(level);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLevelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(levelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLevel() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);

        int databaseSizeBeforeDelete = levelRepository.findAll().size();

        // Delete the level
        restLevelMockMvc
            .perform(delete(ENTITY_API_URL_ID, level.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
