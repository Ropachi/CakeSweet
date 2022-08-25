//コースモデル用リポジトリ
package abc.cakesweet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import abc.cakesweet.CosData;

@Repository
public interface CosDataRepository extends JpaRepository<CosData, Long> {
    //コースデータをIDで検索
    public Optional<CosData> findByCosid(Long cosid);

    //個人データをIDで検索
    public List<CosData> findByMyid(Long myid);

    //コースデータをIDで削除
    public void deleteByCosid(Long cosid);
}