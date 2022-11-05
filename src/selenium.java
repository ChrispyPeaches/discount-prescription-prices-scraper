import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class selenium {
    public static ChromeDriver driver;
    public static void Setup(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    public static void main(String[] args){
        String zipCode = "71272";
        Medication DextroamphetamineAmphetamine = new Medication(
                "Dextroamphetamine-amphetamine",
                15,
                "mg",
                30
        );
        Medication[] medList = {DextroamphetamineAmphetamine};
        try{
            Setup();
            for (Medication medication: medList) {
                RXcom(medication);
                Thread.sleep(1000);
            }

        }
        catch (Exception e){
            System.out.println(e);
        }
        finally{
            Teardown();
        }
    }
    public static boolean RXcom(Medication medication) throws InterruptedException {
        // Go to rx.com
        driver.get("https://www.rx.com/");
        // Click on the text input
        driver.findElement(By.cssSelector("input[name='txt_itemname']")).click();
        // Fill out the text input
        driver.findElement(By.cssSelector("#txt_itemname")).sendKeys(medication.name);
        // Wait for the options to appear
        TimeUnit.SECONDS.sleep(1);
        // Select the top option
        driver.findElement(By.cssSelector("#ui-id-2 li:first-of-type a")).click();
        // Wait for the initial Javascript on the page to finish
        TimeUnit.SECONDS.sleep(5);
        // Select the pop-up menu's close button
        driver.findElement(By.cssSelector("#closebutton")).click();

        driver.findElement(By.cssSelector("#dropdown-select-package")).click();
        List<WebElement> options = driver.findElements(By.cssSelector("#dropdown-menu-package li"));
        TimeUnit.SECONDS.sleep(1);
        for (WebElement option: options) {
            if(option.getAttribute("data-field-value").contains(String.valueOf(medication.dosageAmnt))){
                option.click();
            }
        }
        return true;
    }
    public static void Teardown(){
        driver.close();
        driver.quit();
    }
}
