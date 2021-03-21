package MakeUs.Moira.domain.userPortfolio.userSchool;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SchoolInfoRepo extends JpaRepository<SchoolInfo, Long> {
    List<SchoolInfo> findBySchoolNameContaining(String keyword);
}
