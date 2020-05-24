package store.store;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class StoreApplication extends Application {
    private static ConfigurableApplicationContext springContext;
    private FXMLLoader fxmlLoader;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(StoreApplication.class);
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(springContext::getBean);
    }

    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        SceneManager.setStage(primaryStage);
        SceneManager.addScene("shopsPanel", "fxml/shops-page.fxml");
        SceneManager.addScene("mainPage", "fxml/main-page.fxml");
        SceneManager.addScene("transactionPage", "fxml/transactions-page.fxml");
        SceneManager.addScene("clientsPage", "fxml/clients-page.fxml");
        SceneManager.addScene("addShopPage", "fxml/add-shop-page.fxml");
        SceneManager.addScene("bookPage", "fxml/books-page.fxml");
        SceneManager.renderScene("shopsPanel");
    }

    @Override
    public void stop() {
        springContext.close();
    }
}
