package antifraud.example.antifraud.repo;

import antifraud.example.antifraud.dto.AdminLinks;
import antifraud.example.antifraud.entity.Links;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRepo extends JpaRepository<Links, Long> {
    List<Links> findAllByAdminId(Long adminId);

    @Query(value = """
            select
              l.id,
              a.fullname,
              l.generated_link,
              l.created_at,
              l.expires_at,
              l.is_expired
              from links l join admin_details a on l.admin_id = a.id""",  nativeQuery = true)
    List<AdminLinks> findLinksAll();

    Links findByGeneratedLink(String generatedLink);

    @Query(value = """
            select
              l.id,
              a.fullname,
              l.generated_link,
              l.created_at,
              l.expires_at,
              l.is_expired
              from links l join admin_details a on l.admin_id = a.id
              where l.admin_id=:adminId""",  nativeQuery = true)
    List<AdminLinks> findLinksAllByAdminId(@Param("adminId") Long adminId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("""
            update Links
            set isExpired=TRUE
            where isExpired=FALSE and id in :linkIds""")
    void updateLinkStatus(@Param("linkIds") List<Long> linkIds);

    Integer countByIsExpired(Boolean isExpired);

    @Query(value = """
            select count(*) from links where CURRENT_TIMESTAMP < expires_at""",  nativeQuery = true)
    Integer countByIsExpired();
}
