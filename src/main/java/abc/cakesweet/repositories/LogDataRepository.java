//ログイン用データモデルリポジトリ
package abc.cakesweet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import abc.cakesweet.LogData;

import java.util.Optional;

@Repository
public interface LogDataRepository extends JpaRepository<LogData, Long> {
    //ログイン用個人をIDで検索
    public Optional<LogData> findByLogid(long logid);
}

