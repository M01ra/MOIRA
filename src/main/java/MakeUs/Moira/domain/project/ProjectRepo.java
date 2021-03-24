package MakeUs.Moira.domain.project;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {
    String findByOrderPage =
            "SELECT DISTINCT P.ID AS id, P.PROJECT_TITLE AS title, P.PROJECT_IMAGE_URL AS imageUrl,  P.HIT_COUNT AS hitCount, P.LIKE_COUNT AS likeCount, P.MODIFIED_DATE AS modifiedDate, NICKNAME AS writer\n" +
            "FROM PROJECT as P\n" +
            "INNER JOIN PROJECT_DETAIL AS PD ON P.PROJECT_DETAIL_ID = PD.ID\n" +
            "INNER JOIN PROJECT_POSITION AS PP ON PD.ID = PP.PROJECT_DETAIL_ID\n" +
            "INNER JOIN PROJECT_HASHTAG AS PH ON P.ID = PH.PROJECT_ID\n" +
            "INNER JOIN HASHTAG AS H ON PH.PROJECT_HASHTAG_ID = H.ID\n" +
            "INNER JOIN (SELECT DISTINCT NICKNAME, PROJECT_ID\n" +
            "FROM USER_PROJECT AS UP\n" +
            "INNER JOIN USER AS U\n" +
            "WHERE UP.ROLE_TYPE = 'LEADER' AND UP.USER_HISTORY_ID = U.USER_HISTORY_ID) AS T\n" +
            "ON P.ID = T.PROJECT_ID\n" +
            "WHERE P.PROJECT_STATUS = 'RECRUITING'\n",

            findByTag = "AND H.HASHTAG_NAME IN :tag\n",
            findByKeyword = "AND P.PROJECT_TITLE LIKE %:keyword%\n";


    @Query(
            value = findByOrderPage,
            nativeQuery = true
    )
    public List<ProjectsResponseInterface> findProjectsByOrderPage(Pageable pageable);

    @Query(
            value = findByOrderPage + findByTag,
            nativeQuery = true
    )
    public List<ProjectsResponseInterface> findProjectsByOrderPageTag(@Param("tag") String[] tag, Pageable pageable);

    @Query(
            value = findByOrderPage + findByKeyword,
            nativeQuery = true
    )
    public List<ProjectsResponseInterface> findProjectsByOrderPageKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query(
            value = findByOrderPage + findByTag + findByKeyword,
            nativeQuery = true
    )
    public List<ProjectsResponseInterface> findProjectsByOrderPageTagKeyword(@Param("tag") String[] tag, @Param("keyword") String keyword, Pageable pageable);

    public static interface ProjectsResponseInterface {
        Long getId();
        String getTitle();
        String getImageUrl();
        Integer getHitCount();
        Integer getLikeCount();
        LocalDateTime getModifiedDate();
        String getWriter();
    }

}
