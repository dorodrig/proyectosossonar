package co.edu.sena.service.impl;

import co.edu.sena.domain.Enrollment;
import co.edu.sena.repository.EnrollmentRepository;
import co.edu.sena.service.EnrollmentService;
import co.edu.sena.service.dto.EnrollmentDTO;
import co.edu.sena.service.mapper.EnrollmentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Enrollment}.
 */
@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private final Logger log = LoggerFactory.getLogger(EnrollmentServiceImpl.class);

    private final EnrollmentRepository enrollmentRepository;

    private final EnrollmentMapper enrollmentMapper;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, EnrollmentMapper enrollmentMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.enrollmentMapper = enrollmentMapper;
    }

    @Override
    public EnrollmentDTO save(EnrollmentDTO enrollmentDTO) {
        log.debug("Request to save Enrollment : {}", enrollmentDTO);
        Enrollment enrollment = enrollmentMapper.toEntity(enrollmentDTO);
        enrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toDto(enrollment);
    }

    @Override
    public EnrollmentDTO update(EnrollmentDTO enrollmentDTO) {
        log.debug("Request to save Enrollment : {}", enrollmentDTO);
        Enrollment enrollment = enrollmentMapper.toEntity(enrollmentDTO);
        enrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toDto(enrollment);
    }

    @Override
    public Optional<EnrollmentDTO> partialUpdate(EnrollmentDTO enrollmentDTO) {
        log.debug("Request to partially update Enrollment : {}", enrollmentDTO);

        return enrollmentRepository
            .findById(enrollmentDTO.getId())
            .map(existingEnrollment -> {
                enrollmentMapper.partialUpdate(existingEnrollment, enrollmentDTO);

                return existingEnrollment;
            })
            .map(enrollmentRepository::save)
            .map(enrollmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnrollmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Enrollments");
        return enrollmentRepository.findAll(pageable).map(enrollmentMapper::toDto);
    }

    /**
     *  Get all the enrollments where Student is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EnrollmentDTO> findAllWhereStudentIsNull() {
        log.debug("Request to get all enrollments where Student is null");
        return StreamSupport
            .stream(enrollmentRepository.findAll().spliterator(), false)
            .filter(enrollment -> enrollment.getStudent() == null)
            .map(enrollmentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnrollmentDTO> findOne(Long id) {
        log.debug("Request to get Enrollment : {}", id);
        return enrollmentRepository.findById(id).map(enrollmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Enrollment : {}", id);
        enrollmentRepository.deleteById(id);
    }
}
