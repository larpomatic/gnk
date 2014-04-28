package org.gnk.selenium

import grails.test.mixin.TestFor
import org.gnk.selectintrigue.SelectIntrigueController
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.support.ui.Select

import java.sql.Time
import java.text.SimpleDateFormat

/**
 * Created by pico on 16/04/2014.
 */
@TestFor(SelectIntrigueController)
class createGnTests {

    WebDriver driver = new FirefoxDriver()

    void testClickToCreateGn() {
        driver.get("http://localhost:8090/gnk/")
        WebElement we = driver.findElement(By.cssSelector(".how-to a"))
        we.click()
    }

    // Fill a field declared by is ID
    void fillInputById(String id, String value) {

        WebElement we = driver.findElement(By.id(id))
        we.sendKeys(value)
    }

    // Check all the boxes of a page
    void checkRandomBox() {

        List<WebElement> lwe = driver.findElements(By.cssSelector("[type='checkbox']"))
        int n = lwe.size()
        Random r = new Random()
        int count = r.nextInt(n)
        while (count != 0)
        {
            int i = r.nextInt(n)
            print lwe.get(i)
            lwe.get(i).click()
        }
    }

//    void checkRandomDropdownlist(String id)
//    {
//        WebElement we = driver.findElement(By.id(id))
//        List<WebElement> options = driver.findElement(By.)
//        Random r = new Random()
//        int count = r.nextInt()
//        options.get(count).click()
//    }

    void testGnCreation() {

        driver.get("http://localhost:8090/gnk/")
        WebElement we = driver.findElement(By.cssSelector(".how-to a")) // [type='checkbox']
        we.click()
        we = driver.findElement(By.linkText("Création/Edition"))
        we.click()
        fillInputById("name", "testSelenium")
        Random r = new Random()
        int tmp = r.nextInt(10)
        fillInputById("gnPIPMin", "5")

        fillInputById("gnPIPMax", "50")

        fillInputById("gnDuration", "10")

        fillInputById("gnNbPlayers", "8")

        fillInputById("gnNbWomen", "2")

        fillInputById("gnNbMen", "3")
        we = driver.findElement(By.id("gnDate"))
        we.sendKeys("2014-04-12")
        we = driver.findElement(By.id("gnDateHour"))
        we.sendKeys("11:06")
        we = driver.findElements(By.className("btn")).get(2) // tags
        we.click()
        we = driver.findElement(By.id("tags_1")).click()
        we = driver.findElement(By.id("tags_2")).click()
        we = driver.findElement(By.id("tags_3")).click()
        we = driver.findElement(By.cssSelector("#tagsModal .close")).click() // close
        we = driver.findElement(By.id("t0Hour"))
        we.sendKeys("11:10")
        we = driver.findElements(By.className("btn")).get(3) // tagsEvenmential
        we.click()
        we = driver.findElement(By.id("tagsEvenemential_1")).click()
        we = driver.findElement(By.id("tagsEvenemential_2")).click()
        we = driver.findElement(By.id("tagsEvenemential_6")).click()
        we = driver.findElement(By.cssSelector("#tagsEvenementialModal .close")).click() // close
        we = driver.findElement(By.id("t0Date"))
        we.sendKeys("2014-04-13")
        we = driver.findElements(By.className("btn")).get(4) // tagsMainstream
        we.click()
        we = driver.findElement(By.id("tagsMainstream_1")).click()
        we = driver.findElement(By.id("tagsMainstream_2")).click()
        we = driver.findElement(By.id("tagsMainstream_7")).click()
        we = driver.findElement(By.cssSelector("#tagsMainstreamModal .close")).click() // close
        we = driver.findElements(By.className("btn-primary")).first() // Update button
        we.click()
        we = driver.findElement(By.cssSelector("#roleToPersoFrom [type=\"submit\"]")).click()
        we = driver.findElement(By.cssSelector("input[value=\"Substitution\"]")).click()
    }
}
