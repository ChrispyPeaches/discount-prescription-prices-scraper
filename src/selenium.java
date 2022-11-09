import io.github.bonigarcia.wdm.WebDriverManager;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class selenium {
    public static ChromeDriver driver;
    public static void Setup(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    public static void main(String[] args) {
        String zipCode = "71272";
        Medication metformin = new Medication(
                "metformin",
                1000,
                "mg",
                60
        );
        Medication[] medList = {metformin};
        try{
            Setup();
            for (Medication medication: medList) {
                RXcom(medication);
                Thread.sleep(1000);
            }

        }
        catch (Exception e){
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        finally{
            Teardown();
        }
    }
    public static void RXcom(Medication medication) throws InterruptedException {
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
        TimeUnit.SECONDS.sleep(1);

        // Select given dosage
        // Click the dosage dropdown menu
        driver.findElement(By.cssSelector("#dropdown-select-package")).click();
        // Grab the options of the dropdown menu
        List<WebElement> dosgeOptions = driver.findElements(By.cssSelector("#dropdown-menu-package li"));
        // Check each option for a match to the given dosage
        for (WebElement option: dosgeOptions) {
            if(option.getAttribute("data-field-value").contains(String.valueOf(medication.dosageAmnt))
                    && option.getAttribute("data-field-value").toUpperCase().contains(medication.dosageUnit)){
                // If there's a match, select it.
                option.click();
            }
        }
        // Wait for page to load
        TimeUnit.SECONDS.sleep(3);

        /*
         * Select given amount
         */
        // Click the amount dropdown menu
        driver.findElement(By.cssSelector("#dropdown-select-qty")).click();
        TimeUnit.MILLISECONDS.sleep(200);
        // Grab the options of the dropdown menu
        List<WebElement> Amntoptions = driver.findElements(By.cssSelector("#dropdown-menu-qty li"));
        // Check each option for a match to the given monthly count
        for (WebElement option: Amntoptions) {
            if(option.getAttribute("data-field-value").contains(String.valueOf(medication.monthlyCnt))){
                // If there's a match, select it.
                option.click();
            }
        }
        // Wait for page to load
        TimeUnit.SECONDS.sleep(5);

        /*
         * Loop through each of the results and retrieve the pharmacy name,
         */
        List<Result> results = new ArrayList<>();
        List<WebElement> elements = driver.findElements(By.cssSelector(".search-result__item"));
        for (WebElement element : elements) {
            // Get the pharmacy name
            WebElement logoOrNameElement = null;
            try{
                logoOrNameElement = element.findElement(By.cssSelector("h4 b, img:first-of-type"));
            } catch (Exception e) {
                continue;
            }
            String pharmaName = "";
            if(logoOrNameElement.getTagName().contains("img")){
                pharmaName = logoOrNameElement.getAttribute("src");
                pharmaName = pharmaName.substring(pharmaName.lastIndexOf('-') + 1, pharmaName.lastIndexOf('.'));
            } else if (logoOrNameElement.getTagName().contains("b")){
                pharmaName = logoOrNameElement.getText();
            }
            // Get the pharmacy addresses
            List<String> addresses = new ArrayList<>();
            var addressContainerSelector = element
                    .findElement(By.cssSelector("a:first-of-type"))
                    .getAttribute("href");
            addressContainerSelector = addressContainerSelector
                    .substring(addressContainerSelector
                    .lastIndexOf('#'));
            for (WebElement addressElement: driver.findElements(By.cssSelector(addressContainerSelector + " h4"))) {
                String innerText = addressElement.getAttribute("innerHTML");
                var innerHtml = Jsoup.parse(innerText);
                innerHtml.select("span").remove();
                innerText = innerHtml.select("body").text();
                addresses.add(innerText);
            }
            float price = Float.parseFloat(element.findElement(By.cssSelector(".price-part")).getText());

            TimeUnit.SECONDS.sleep(2);
            var result = new Result(
                    pharmaName,
                    addresses,
                    price
            );
            results.add(result);
            System.out.println(result);
        }

    }
    public static void Teardown(){
        driver.quit();
        driver.close();
    }
}
