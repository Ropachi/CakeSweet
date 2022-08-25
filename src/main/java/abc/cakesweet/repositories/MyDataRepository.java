//個人データモデルリポジトリ
package abc.cakesweet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import abc.cakesweet.MyData;

@Repository
public interface MyDataRepository extends JpaRepository<MyData, Long> {
    //個人データを全て検索
    public List<MyData> findAll();

    //個人データをIDで検索
    public Optional<MyData> findById(long id);
}