package selenium

//import com.sun.tools.doclets.internal.toolkit.resources.doclets
import grails.test.mixin.TestFor
import org.gnk.selectintrigue.SelectIntrigueController
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver

/**
 *
 */
@TestFor(SelectIntrigueController)
class CheckGnTests {

    int nbF = 0
    int nbM = 0
    int nbChar = 0
    WebDriver driver = new FirefoxDriver()

    // ================== HELPERS ======================================

    void testClickToCreateGn() {
        driver.get("http://localhost:8090/gnk/")
        WebElement we = driver.findElement(By.id("username")).sendKeys("pico.2607@gmail.com")
        we = driver.findElement(By.id("password")).sendKeys("gnk")
        we = driver.findElement(By.id("submit")).click()
        we = driver.findElement(By.cssSelector(".how-to a"))
        we.click()
    }

    // Fill a field declared by is ID
    void fillInputById(String id, String value) {

        WebElement we = driver.findElement(By.id(id))
        we.sendKeys(value)
    }

    // Fill a integer field declared by is ID
    void fillIntegerInputById(String id, String value) {

        JavascriptExecutor js = (JavascriptExecutor) driver
        js.executeScript("javascript:document.getElementById(\"" + id + "\").value=" + value + ";")
    }

    // Check all the boxes of a page
    void checkRandomBox() {

        List<WebElement> lwe = driver.findElements(By.cssSelector("[type='checkbox']"))
        int n = lwe.size()
        Random r = new Random()
        int count = r.nextInt(n)
        while (count != 0) {
            int i = r.nextInt(n)
            print lwe.get(i)
            lwe.get(i).click()
        }
    }

    // ====================================================================

    void testGoToRoleToPerso()
    {
        driver.get("http://localhost:8090/gnk/")
        WebElement we = driver.findElement(By.id("username")).sendKeys("pico.2607@gmail.com")
        we = driver.findElement(By.id("password")).sendKeys("gnk")
        we = driver.findElement(By.id("submit")).click()
        we = driver.findElement(By.cssSelector(".how-to a")) // [type='checkbox']
        we.click()

        // -------- BEGIN EDIT GN --------
        we = driver.findElement(By.linkText("Création/Edition"))
        we.click()

        fillInputById("name", "testSelenium")
        fillIntegerInputById("gnPIPMin", "5")
        fillIntegerInputById("gnPIPMax", "50")
        fillIntegerInputById("gnDuration", "10")
        fillIntegerInputById("gnNbPlayers", "8")
        fillIntegerInputById("gnNbWomen", "2")
        fillIntegerInputById("gnNbMen", "3")
        fillInputById("gnDate", "2014-04-12")
        fillInputById("gnDateHour", "11:06")

        we = driver.findElements(By.className("btn")).get(2) // tags
        we.click()
        we = driver.findElement(By.id("tags_33026")).click()
        we = driver.findElement(By.id("tags_33018")).click()
        we = driver.findElement(By.id("tags_33060")).click()
        we = driver.findElement(By.cssSelector("#tagsModal .close")).click() // close
        fillInputById("t0Hour", "11:10")
        we = driver.findElements(By.className("btn")).get(3) // tagsEvenmential
        we.click()
        we = driver.findElement(By.id("tagsEvenemential_33018")).click()
        we = driver.findElement(By.id("tagsEvenemential_33060")).click()
        we = driver.findElement(By.id("tagsEvenemential_33051")).click()
        we = driver.findElement(By.cssSelector("#tagsEvenementialModal .close")).click() // close
        fillInputById("t0Date", "2014-04-13")
        we = driver.findElements(By.className("btn")).get(4) // tagsMainstream
        we.click()
        we = driver.findElement(By.id("tagsMainstream_33026")).click()
        we = driver.findElement(By.id("tagsMainstream_33018")).click()
        we = driver.findElement(By.id("tagsMainstream_33060")).click()
        we = driver.findElement(By.cssSelector("#tagsMainstreamModal .close")).click() // close
        we = driver.findElements(By.className("btn-primary")).first() // Update button
        we.click()
        // -------- END EDIT GN --------
        // -------- BEGIN ROLE2PERSO --------
        we = driver.findElement(By.cssSelector("#roleToPersoFrom [type=\"submit\"]")).click()
        // -------- END ROLE2PERSO
    }

    // ====================================================================

    void getNbCharacters(HashMap m)
    {
        for (Map.Entry<String, String> e : m.entrySet())
        {
            if (e.getKey() == "Genre")
                nbChar++
        }
    }

    boolean checkNbChar()
    {
        if (nbChar == 8)
            return true
        else
            throw new Exception("checkNbCharacters: False")
    }

    void getSexChar(HashMap m)
    {

        for (Map.Entry<String, String> e : m.entrySet())
        {
            if (e.getKey() == "Genre" && e.getValue() == "M")
            {
                print("Un Mec")
                nbM++
            }
            else if (e.getKey() == "Genre" && e.getValue() == "F")
            {
                print("Une Nana")
                nbF++
            }
        }
    }

    boolean checkSexChar()
    {
        if (nbF >= 2 && nbM >= 3)
            return true
        else
            throw new Exception("checkSexChar: False")
    }

    boolean checkPip(HashMap m)
    {
        for (Map.Entry<String, String> e : m.entrySet())
        {
            if (e.getKey() == "Nombre de PIP")
            {
                if (e.getValue().toInteger() < 5 || e.getValue().toInteger() > 50)
                    throw new Exception("checkNbPip: False")
                print("POUET")
            }
        }
            return true
    }

    // ====================================================================


    void testCheckInformation()
    {
        testGoToRoleToPerso()
        driver.findElement(By.cssSelector("#accordionStat0 a")).click()
        List<WebElement> tables = new LinkedList<WebElement>();
        WebElement we = driver.findElement(By.id("collapseStat0"))
        int j=0

        while (j < 8)
        {
            tables.push(we)
            driver.findElement(By.cssSelector("#accordionStat" + j + " a")).click()
            we = driver.findElement(By.id("collapseStat"+j))
            j++
        }
        HashMap<String, String> tab = new HashMap<String,String>()

        String key
        String value
        for(WebElement e : tables)
        {
            List<WebElement> table = e.findElements(By.cssSelector("td"))
            int i  = 0
            for(WebElement col : table)
            {
                if (i % 2 == 0)
                    key = col.getAttribute('textContent').trim()
                else
                {
                    value = col.getAttribute('textContent').trim()
                    tab.put(key, value)
//                    print(key + " = " + value);
                    key = ""
                    value = ""
                }
                i++
            }
            checkPip(tab)
            getSexChar(tab)
            getNbCharacters(tab)
        }
        checkSexChar()
        checkNbChar()
    }
}
