package MakeUs.Moira.domain.project.projectApply;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProjectApplyRepo extends JpaRepository<ProjectApply, Long> {
    public List<ProjectApply> findAllByApplicantId(Long applicantId);
}
