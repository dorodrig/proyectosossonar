package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.Achievement;
import co.edu.sena.domain.enumeration.StateAchievement;
import co.edu.sena.repository.AchievementRepository;
import co.edu.sena.service.AchievementService;
import co.edu.sena.service.dto.AchievementDTO;
import co.edu.sena.service.mapper.AchievementMapper;
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
 * Integration tests for the {@link AchievementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AchievementResourceIT {

    private static final String DEFAULT_ACHIEVEMENT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ACHIEVEMENT_DESCRIPTION = "BBBBBBBBBB";

    private static final StateAchievement DEFAULT_STATUS_ACHIEVEMENT = StateAchievement.Active;
    private static final StateAchievement UPDATED_STATUS_ACHIEVEMENT = StateAchievement.Inactive;

    private static final String ENTITY_API_URL = "/api/achievements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AchievementRepository achievementRepository;

    @Mock
    private AchievementRepository achievementRepositoryMock;

    @Autowired
    private AchievementMapper achievementMapper;

    @Mock
    private AchievementService achievementServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAchievementMockMvc;

    private Achievement achievement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Achievement createEntity(EntityManager em) {
        Achievement achievement = new Achievement()
            .achievementDescription(DEFAULT_ACHIEVEMENT_DESCRIPTION)
            .statusAchievement(DEFAULT_STATUS_ACHIEVEMENT);
        return achievement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Achievement createUpdatedEntity(EntityManager em) {
        Achievement achievement = new Achievement()
            .achievementDescription(UPDATED_ACHIEVEMENT_DESCRIPTION)
            .statusAchievement(UPDATED_STATUS_ACHIEVEMENT);
        return achievement;
    }

    @BeforeEach
    public void initTest() {
        achievement = createEntity(em);
    }

    @Test
    @Transactional
    void createAchievement() throws Exception {
        int databaseSizeBeforeCreate = achievementRepository.findAll().size();
        // Create the Achievement
        AchievementDTO achievementDTO = achievementMapper.toDto(achievement);
        restAchievementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(achievementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeCreate + 1);
        Achievement testAchievement = achievementList.get(achievementList.size() - 1);
        assertThat(testAchievement.getAchievementDescription()).isEqualTo(DEFAULT_ACHIEVEMENT_DESCRIPTION);
        assertThat(testAchievement.getStatusAchievement()).isEqualTo(DEFAULT_STATUS_ACHIEVEMENT);
    }

    @Test
    @Transactional
    void createAchievementWithExistingId() throws Exception {
        // Create the Achievement with an existing ID
        achievement.setId(1L);
        AchievementDTO achievementDTO = achievementMapper.toDto(achievement);

        int databaseSizeBeforeCreate = achievementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAchievementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(achievementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAchievementDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = achievementRepository.findAll().size();
        // set the field null
        achievement.setAchievementDescription(null);

        // Create the Achievement, which fails.
        AchievementDTO achievementDTO = achievementMapper.toDto(achievement);

        restAchievementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(achievementDTO))
            )
            .andExpect(status().isBadRequest());

        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAchievements() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);

        // Get all the achievementList
        restAchievementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(achievement.getId().intValue())))
            .andExpect(jsonPath("$.[*].achievementDescription").value(hasItem(DEFAULT_ACHIEVEMENT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].statusAchievement").value(hasItem(DEFAULT_STATUS_ACHIEVEMENT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAchievementsWithEagerRelationshipsIsEnabled() throws Exception {
        when(achievementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAchievementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(achievementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAchievementsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(achievementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAchievementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(achievementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAchievement() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);

        // Get the achievement
        restAchievementMockMvc
            .perform(get(ENTITY_API_URL_ID, achievement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(achievement.getId().intValue()))
            .andExpect(jsonPath("$.achievementDescription").value(DEFAULT_ACHIEVEMENT_DESCRIPTION))
            .andExpect(jsonPath("$.statusAchievement").value(DEFAULT_STATUS_ACHIEVEMENT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAchievement() throws Exception {
        // Get the achievement
        restAchievementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAchievement() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);

        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();

        // Update the achievement
        Achievement updatedAchievement = achievementRepository.findById(achievement.getId()).get();
        // Disconnect from session so that the updates on updatedAchievement are not directly saved in db
        em.detach(updatedAchievement);
        updatedAchievement.achievementDescription(UPDATED_ACHIEVEMENT_DESCRIPTION).statusAchievement(UPDATED_STATUS_ACHIEVEMENT);
        AchievementDTO achievementDTO = achievementMapper.toDto(updatedAchievement);

        restAchievementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, achievementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(achievementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
        Achievement testAchievement = achievementList.get(achievementList.size() - 1);
        assertThat(testAchievement.getAchievementDescription()).isEqualTo(UPDATED_ACHIEVEMENT_DESCRIPTION);
        assertThat(testAchievement.getStatusAchievement()).isEqualTo(UPDATED_STATUS_ACHIEVEMENT);
    }

    @Test
    @Transactional
    void putNonExistingAchievement() throws Exception {
        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();
        achievement.setId(count.incrementAndGet());

        // Create the Achievement
        AchievementDTO achievementDTO = achievementMapper.toDto(achievement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAchievementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, achievementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(achievementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAchievement() throws Exception {
        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();
        achievement.setId(count.incrementAndGet());

        // Create the Achievement
        AchievementDTO achievementDTO = achievementMapper.toDto(achievement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAchievementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(achievementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAchievement() throws Exception {
        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();
        achievement.setId(count.incrementAndGet());

        // Create the Achievement
        AchievementDTO achievementDTO = achievementMapper.toDto(achievement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAchievementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(achievementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAchievementWithPatch() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);

        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();

        // Update the achievement using partial update
        Achievement partialUpdatedAchievement = new Achievement();
        partialUpdatedAchievement.setId(achievement.getId());

        restAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAchievement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAchievement))
            )
            .andExpect(status().isOk());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
        Achievement testAchievement = achievementList.get(achievementList.size() - 1);
        assertThat(testAchievement.getAchievementDescription()).isEqualTo(DEFAULT_ACHIEVEMENT_DESCRIPTION);
        assertThat(testAchievement.getStatusAchievement()).isEqualTo(DEFAULT_STATUS_ACHIEVEMENT);
    }

    @Test
    @Transactional
    void fullUpdateAchievementWithPatch() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);

        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();

        // Update the achievement using partial update
        Achievement partialUpdatedAchievement = new Achievement();
        partialUpdatedAchievement.setId(achievement.getId());

        partialUpdatedAchievement.achievementDescription(UPDATED_ACHIEVEMENT_DESCRIPTION).statusAchievement(UPDATED_STATUS_ACHIEVEMENT);

        restAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAchievement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAchievement))
            )
            .andExpect(status().isOk());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
        Achievement testAchievement = achievementList.get(achievementList.size() - 1);
        assertThat(testAchievement.getAchievementDescription()).isEqualTo(UPDATED_ACHIEVEMENT_DESCRIPTION);
        assertThat(testAchievement.getStatusAchievement()).isEqualTo(UPDATED_STATUS_ACHIEVEMENT);
    }

    @Test
    @Transactional
    void patchNonExistingAchievement() throws Exception {
        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();
        achievement.setId(count.incrementAndGet());

        // Create the Achievement
        AchievementDTO achievementDTO = achievementMapper.toDto(achievement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, achievementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(achievementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAchievement() throws Exception {
        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();
        achievement.setId(count.incrementAndGet());

        // Create the Achievement
        AchievementDTO achievementDTO = achievementMapper.toDto(achievement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(achievementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAchievement() throws Exception {
        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();
        achievement.setId(count.incrementAndGet());

        // Create the Achievement
        AchievementDTO achievementDTO = achievementMapper.toDto(achievement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(achievementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAchievement() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);

        int databaseSizeBeforeDelete = achievementRepository.findAll().size();

        // Delete the achievement
        restAchievementMockMvc
            .perform(delete(ENTITY_API_URL_ID, achievement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
