package MakeUs.Moira.domain.project.projectApply;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectApplyRepo extends JpaRepository<ProjectApply, Long> {
    int countByApplicant_Id(Long applicantId);
}
