package controllers;

import io.ebean.Ebean;
import models.User;
import models.LoginForm;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;

/**
 * Controller that render login form and hanlde all request for login and logout.
 */
public class LoginController extends Controller {
    @Inject
    FormFactory formFactory;
    private static Logger.ALogger logger = Logger.of(LoginController.class);
    /**
     * Render login form
     * @return Response with form in it
     */
    public Result renderLoginForm(){

        Form<LoginForm> form = formFactory.form(LoginForm.class);
        return ok(views.html.login.render(form));
    }
    /**
     * Handle form with data, parse it and serching for such user in database.
     * If success it redirects to page depends on user`s role and put username in session.
     * If there were errors it redirects to loginPage again.
     * @return Responses with redirects to pages by calling their controllers.
     */
    public Result checkingLoginForm(){
        Integer error;
        Form<LoginForm> bookForm = formFactory.form(LoginForm.class).bindFromRequest();

        if(bookForm.hasGlobalErrors()||bookForm.hasErrors() ){
            error = 1; // неверный логин или пароль

            mainPageController.error=error;

            return redirect(routes.mainPageController.test());
        }
        LoginForm book = bookForm.get();
        System.out.println(book.toString());
        List<User> users = Ebean.find(User.class).where().eq("login", book.getLogin()).findList();

        User user = users.get(0);
        if( user.getPassword().equals(book.getPassword())) {

            error=0;
            mainPageController.error=error; // все хорошо, ошибок нет
            logger.info("Пользователь " + user.getLogin() + " успешно авторизовался");
            session().put("username", book.getLogin());
            logger.info("Пользователь " + user.getLogin() + " авторизовался ");
            return redirect(routes.mainPageController.projectPage(book.getLogin(),user.getAdmin()));

        }
        session().remove("username");
        error = 1; // неверный логин или пароль

        mainPageController.error=error;


        return redirect(routes.mainPageController.test());
    }

    /**
     * Handle logout request and remove user`s name form session.
     * @return Response with redirect to login page.
     */
    public Result logout(){
        logger.info("Пользователь " + session().get("username") + " разлогинился");
        session().remove("username");
        return redirect(routes.mainPageController.projectPage("",false));
    }

}
