//メインコントロールクラス
package abc.cakesweet;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import abc.cakesweet.repositories.LogDataRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.Query;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

import abc.cakesweet.repositories.MyDataRepository;
import abc.cakesweet.repositories.CosDataRepository;


@Controller
public class HelloController {
    @Autowired
    MyDataRepository repository;

    @Autowired
    CosDataRepository cosrepository;

    @Autowired
    LogDataRepository logrepository;

    Long login_id = null;
    String login_name = null;
    String login_psw = null;

    @PostConstruct
    public void init() {
        //logdata
        LogData d1 = new LogData();
        d1.setLogid((long) 1);
        d1.setLogname("kiOYuT49HiJ8");
        d1.setLogpsw("Uy75TvE9NpO6J");
        logrepository.saveAndFlush(d1);
    }

    @PersistenceContext
    EntityManager entityManager;  //field定義

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(
            @ModelAttribute("formModel") MyData mydata,
            @ModelAttribute("logModel") LogData logdata,
            ModelAndView mav) {
        mav.setViewName("index");

        //Home等ボタン表示内容設定のために logname を設定
        String logname = "kiOYuT49HiJ8";
        Optional<LogData> list = logrepository.findByLogid((long) 1);
        if (list.get().getLogname() != "kiOYuT49HiJ8") {
            logname = list.get().getLogname();
            mav.addObject("logname", logname);
        }

        //コース申し込み等の際の判断のための login_id をここで設定。
        if ((login_name != null) && (login_psw != null)) {
            Optional<MyData> data2 = findByNameAndPsw(login_name, login_psw);
            if (data2 != null) {
                login_id = data2.get().getId();
            }
        }
        return mav;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(
            ModelAndView mav) {
        mav.setViewName("login");
        String mes = "";
        if (ret == 1)
            mes = "";
        if (ret == 2)
            mes = "お名前あるいはパスワードが一致しません";

        mav.addObject("msg", mes);
        return mav;
    }

    int ret = 0;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login2(
            @RequestParam("name") String login_name,
            @RequestParam("psw") String login_psw,
            @ModelAttribute("formModel") MyData mydata,
            @ModelAttribute("logModel") LogData logdata,
            ModelAndView mav) {

        try {
            Optional<MyData> data = findByNameAndPsw(login_name, login_psw);
            //入力した名前とパスワードのセットが既存データにあるかどうかチェック
            if (data != null) {  //該当データがあった場合の処理、
                login_id = data.get().getId();
                login_name = data.get().getName();
                login_psw = data.get().getPsw();

                //index.htmlへ値を渡すための準備 LogDataへ値をセット
                logdata.setLogid((long) 1);
                logdata.setLogname(login_name);
                logdata.setLogpsw(login_psw);
                updatelog(logdata, mav);

                ret = 1;
            } else {
                ret = 2;  //該当するデータがなかったのでアウト
            }
        } catch (Exception e) {
            ret = 2;
        } finally {
            //return new ModelAndView("redirect:/login");
        }
        if (ret == 1) {
            return new ModelAndView("redirect:/");
        } else {
            return new ModelAndView("redirect:/login");
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(@ModelAttribute("formModel") MyData mydata,
                               ModelAndView mav) {
        mav.setViewName("create");
        String mes = "";
        switch (ret) {
            case 1:
                mes = "その名前とパスワードの組み合わせは既に存在します。";
            case 2:
                mes = "";
            case 3:
                mes = "入力エラーがありました!";
        }
        System.out.println("create:GET mes=" + mes);
        mav.addObject("msg", mes);
        return mav;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @Transactional(readOnly = false)
    public ModelAndView create2(
            @RequestParam("name") String login_name,
            @RequestParam("psw") String login_psw,
            @ModelAttribute("logModel") LogData logdata,
            @ModelAttribute("formModel") @Validated MyData mydata,
            // ↓バリデーションチェックの結果は、このBindingResultという引数で取得
            BindingResult result,
            ModelAndView mav) {

        ModelAndView res = null;
        //既存の名前とパスワードのセットと重複していないかチェック
        try {
            Optional<MyData> data = findByNameAndPsw(login_name, login_psw);
            if (data != null) {
                res = new ModelAndView("redirect:/create");
                ret = 1;     //Out 該当既存データがある、つまり重複している
            } else {
                res = new ModelAndView("redirect:/");
                ret = 2;     //OK 該当既存データが重複してない。
            }
        } catch (Exception e) {
            ret = 1;
        }
        //validation
        // ↓この変数resultにエラー結果が入っている。
        if (result.hasErrors()) {  //エラーがあったかどうかチェック
            Iterable<MyData> list = repository.findAll();
            mav.addObject("datalist", list);
            res = mav;
            ret = 3;  //Error
        } else {
            //mydata更新
            update(mydata, mav);
            //index.htmlへ値を渡すための準備 LogDataへ値をセット
            logdata.setLogid((long) 1);
            logdata.setLogname(login_name);
            logdata.setLogpsw(login_psw);
            updatelog(logdata, mav);
            res = new ModelAndView("redirect:/");
        }
        return res;
    }


    @RequestMapping(value = "/createcos", method = RequestMethod.GET)
    public ModelAndView createcos(
            @ModelAttribute("cosModel") CosData cosdata,
            //@ModelAttribute("formModel") MyData mydata,
            @ModelAttribute("logModel") LogData logdata,
            ModelAndView mav) {
        mav.setViewName("createcos");
        mav.addObject("title", "CreateCos");

        //Home等ボタン表示内容設定のために logname を設定
        String logname = "kiOYuT49HiJ8";
        Optional<LogData> list = logrepository.findByLogid((long) 1);
        if (list.get().getLogname() != "kiOYuT49HiJ8") {
            logname = list.get().getLogname();
            mav.addObject("logname", logname);
        } else {
            logname = "kiOYuT49HiJ8";
        }
        return mav;
    }

    @RequestMapping(value = "/createcos", method = RequestMethod.POST)
    @Transactional(readOnly = false)
    public ModelAndView createcos2(
            @ModelAttribute("cosModel") CosData cosdata,
            ModelAndView mav) {
        if (login_id != null) {
            cosdata.setMyid(login_id);
        }
        updatecos(cosdata, mav);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ModelAndView delete(@PathVariable long id,
                               ModelAndView mav) {
        mav.setViewName("delete");
        mav.addObject("title", "アカウント削除.");
        Optional<MyData> data = repository.findById(id);
        mav.addObject("formModel", data.get());
        return mav;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @Transactional(readOnly = false)
    public ModelAndView remove(@RequestParam long id,
                               ModelAndView mav) {
        repository.deleteById(id);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/deletecos/{cosid}", method = RequestMethod.GET)
    public ModelAndView deletecos(@PathVariable Long cosid,
                                  ModelAndView mav) {
        mav.setViewName("deletecos");
        mav.addObject("title", "Course Delete");
        Optional<CosData> data = cosrepository.findByCosid(cosid);
        mav.addObject("cosModel", data.get());
        return mav;
    }

    @RequestMapping(value = "/deletecos", method = RequestMethod.POST)
    @Transactional(readOnly = false)
    public ModelAndView removecos(@RequestParam Long cosid,
                                  ModelAndView mav) {
        cosrepository.deleteByCosid(cosid);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit(@ModelAttribute MyData mydata,
                             ModelAndView mav) {
        mav.setViewName("edit");
        mav.addObject("title", "アカウント修正.");

        //if (login_id != null) {
        Optional<MyData> data = repository.findById(login_id);
        mav.addObject("formModel", data.get());
        //}
        return mav;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @Transactional(readOnly = false)
    public ModelAndView edit2(@ModelAttribute MyData mydata,
                              BindingResult result,
                              ModelAndView mav) {
        ModelAndView res = null;

        if (!result.hasErrors()) {  //エラーがあったかどうかチェック
            repository.saveAndFlush(mydata);
            res = new ModelAndView("redirect:/edit");
        } else {
            mav.addObject("msg", "Error is occured!");
            res = new ModelAndView("redirect:/edit");
        }
        return res;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(
            @ModelAttribute("formModel") MyData mydata,
            ModelAndView mav) {
        mav.setViewName("logout");
        return mav;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @Transactional(readOnly = false)
    public ModelAndView logout2(@RequestParam("name") String login_name,
                                @RequestParam("psw") String login_psw,
                                @ModelAttribute("formModel") MyData mydata,
                                ModelAndView mav) {
        login_id = null;
        login_name = null;
        login_psw = null;
        String logname = "kiOYuT49HiJ8";
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(
            @ModelAttribute("formModel") MyData mydata,
            ModelAndView mav) {
        mav.setViewName("list");
        mav.addObject("msg", "This is Data List.");
        Iterable<MyData> list = repository.findAll();
        mav.addObject("datalist", list);
        return mav;
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @Transactional(readOnly = false)
    public ModelAndView list2(
            @ModelAttribute("formModel") MyData mydata,
            ModelAndView mav) {
        repository.saveAndFlush(mydata);
        return new ModelAndView("redirect:/");
    }

    //現在の全オーダーリスト表示
    @RequestMapping(value = "/listcos", method = RequestMethod.GET)
    public ModelAndView listcos(
            @ModelAttribute("cosModel") CosData cosdata,
            ModelAndView mav) {
        mav.setViewName("listcos");
        mav.addObject("msg", "This is Course List.");
        List<CosData> list = cosrepository.findAll();
        mav.addObject("datalist", list);
        return mav;
    }

    @RequestMapping(value = "/listcos", method = RequestMethod.POST)
    @Transactional(readOnly = false)
    public ModelAndView listcos2(
            @ModelAttribute("cosModel") CosData cosdata,
            ModelAndView mav) {
        cosrepository.saveAndFlush(cosdata);
        return new ModelAndView("redirect:/");
    }

    public ModelAndView update(MyData mydata,
                               ModelAndView mav) {
        repository.saveAndFlush(mydata);
        return mav;
    }

    public ModelAndView updatecos(CosData cosdata,
                                  ModelAndView mav) {
        cosrepository.saveAndFlush(cosdata);
        return mav;
    }

    public ModelAndView updatelog(LogData logdata,
                                  ModelAndView mav) {
        logrepository.saveAndFlush(logdata);
        return mav;
    }

    public MyData findByName(String sname) {
        String qstr = "SELECT c FROM MyData c WHERE c.name = :fname";
        Query query = entityManager.createQuery(qstr, MyData.class);
        query.setParameter("fname", sname);
        MyData list = (MyData) query.getSingleResult();
        return list;
    }

    public Optional<MyData> findByNameAndPsw(String sname, String spsw) {
        String qstr = "SELECT c FROM MyData c WHERE c.name = :fname and c.psw = :fpsw";
        Query query = entityManager.createQuery(qstr, MyData.class);
        query.setParameter("fname", sname);
        query.setParameter("fpsw", spsw);
        MyData list = (MyData) query.getSingleResult();
        return Optional.ofNullable(list);
    }

    //public Optional<LogData> findByLogid(){
    //    String qstr = "SELECT c FROM LogData c WHERE c.logid = 1";
    //    Query query = entityManager.createQuery(qstr, LogData.class);
    //    LogData list = (LogData) query.getSingleResult();
    //    return Optional.ofNullable(list);
    //}
}
