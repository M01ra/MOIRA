package MakeUs.Moira.domain.userPortfolio.userSchool;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MajorInfoRepo extends JpaRepository<MajorInfo, Long> {
    List<MajorInfo> findByMajorNameContaining(String keyword);
}
