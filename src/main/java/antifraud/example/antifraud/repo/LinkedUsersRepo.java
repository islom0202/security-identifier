package antifraud.example.antifraud.repo;

import antifraud.example.antifraud.dto.LinkedUserRes;
import antifraud.example.antifraud.dto.LinkedUsersRes;
import antifraud.example.antifraud.dto.UsersList;
import antifraud.example.antifraud.entity.LinkedUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkedUsersRepo extends JpaRepository<LinkedUsers, Long> {
    @Query(value = """
            select
              ud.id as user_id,
              ud.user_phone,
              lu.user_code,
              ud.user_ip,
              ud.user_device_id,
              ud.is_fraud,
              lu.clicked_at
              from linked_users lu join user_details ud on lu.user_id = ud.id
              where lu.link_id=:linkId""", nativeQuery = true)
    List<UsersList> findLinksAll(@Param("linkId") Long linkId);

    LinkedUsers findByUserId(Long userId);

    @Query(value = """
            select
                          ud.id as user_id,
                          ud.user_phone,
                          ls.generated_link as link_name,
                          lu.user_code as code,
                          ud.user_ip,
                          ud.user_device_id,
                          lu.clicked_at as submitted_at,
                          ud.is_fraud
                          from linked_users lu right join user_details ud on lu.user_id = ud.id
                          left join links ls on ls.id = lu.link_id
                          where ud.id=:userId""", nativeQuery = true)
    LinkedUsersRes findUser(@Param("userId") Long userId);

}
