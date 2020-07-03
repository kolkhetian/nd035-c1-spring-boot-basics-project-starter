package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests
{
	@LocalServerPort
	private int port;
	private WebDriver driver;

	@BeforeAll
	static void beforeAll()
	{
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach()
	{
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach()
	{
		if (this.driver != null)
		{
			driver.quit();
		}
	}

	@Test
	@Order(1)
	public void loginPageTest()
	{
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(2)
	public void signUpPageTest()
	{
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	@Order(3)
	public void signUpTest()
	{
		final String firstName = "Serdar";
		final String lastName  = "Semiz";
		final String username  = "serdars";
		final String password  = "123456";

		WebDriverWait wait = new WebDriverWait (driver, 30);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://localhost:" + this.port + "/signup");

		JavascriptExecutor jse =(JavascriptExecutor) driver;

		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		wait.until(ExpectedConditions.elementToBeClickable(inputFirstName)).click();

		WebElement inputLastName= driver.findElement(By.id("inputLastName"));
		wait.until(ExpectedConditions.elementToBeClickable(inputLastName)).click();

		WebElement inputUsername= driver.findElement(By.id("inputUsername"));
		wait.until(ExpectedConditions.elementToBeClickable(inputUsername)).click();

		WebElement inputPassword= driver.findElement(By.id("inputPassword"));
		wait.until(ExpectedConditions.elementToBeClickable(inputPassword)).click();

		inputFirstName.sendKeys(firstName);
		inputLastName.sendKeys(lastName);
		inputUsername.sendKeys(username);
		inputPassword.sendKeys(password);

		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		wait.until(ExpectedConditions.elementToBeClickable(buttonSignUp)).click();

		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	@Order(4)
	public void loginTest()
	{
		final String username = "serdars";
		final String password = "123456";

		WebDriverWait wait = new WebDriverWait (driver, 30);

		driver.get("http://localhost:" + this.port + "/login");

		JavascriptExecutor jse =(JavascriptExecutor) driver;

		WebElement usernameElement = driver.findElement(By.id("inputUsername"));
		usernameElement.sendKeys(username);

		WebElement passwordElement = driver.findElement(By.id("inputPassword"));
		passwordElement.sendKeys(password);

		WebElement loginElement = driver.findElement(By.id("buttonLogIn"));
		loginElement.click();
		System.out.println(driver.getTitle());
		Assertions.assertEquals("Home", driver.getTitle());
	}

	@Test
	@Order(5)
	public void createNoteTest()
	{
		String username = "serdars";
		String password = "123456";

		String noteTitle = "Lorem ipsum dolor";
		String noteDescription = "Lorem ipsum dolor sit amet..";

		WebDriverWait wait = new WebDriverWait (driver, 30);

		driver.get("http://localhost:" + this.port + "/login");

		driver.manage().window().maximize();

		JavascriptExecutor jse =(JavascriptExecutor) driver;

		WebElement usernameElement = driver.findElement(By.id("inputUsername"));
		usernameElement.sendKeys(username);

		WebElement passwordElement = driver.findElement(By.id("inputPassword"));
		passwordElement.sendKeys(password);

		WebElement logInElement = driver.findElement(By.id("buttonLogIn"));
		wait.until(ExpectedConditions.elementToBeClickable(logInElement)).click();

		WebElement notesTab = driver.findElement(By.xpath("//a[@href='#nav-notes']"));
		jse.executeScript("arguments[0].click()", notesTab);

		WebElement addNewNoteButton = driver.findElement(By.xpath("//button[@id='addNewNote']"));
		wait.until(ExpectedConditions.elementToBeClickable(addNewNoteButton)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).sendKeys(noteTitle);;

		WebElement noteDescriptionElement = driver.findElement(By.id("note-description"));
		noteDescriptionElement.click();
		noteDescriptionElement.sendKeys(noteDescription);

		WebElement noteSubmit = driver.findElement(By.id("saveNoteChanges"));
		noteSubmit.click();
		Assertions.assertEquals("Home", driver.getTitle());

		jse.executeScript("arguments[0].click()", driver.findElement(By.xpath("//a[@href='#nav-notes']")));

		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> noteList = notesTable.findElements(By.tagName("th"));

		boolean OK = false;

		for (int i = 0; i < noteList.size(); i++)
		{
			WebElement row = noteList.get(i);

			if (!row.isDisplayed())
			{
				jse.executeScript("arguments[0].scrollIntoView(true);", row);
			}

			if (row.getAttribute("innerHTML").equals(noteTitle))
			{
				OK = true;
				break;
			}
		}

		Assertions.assertTrue(OK);
	}

	@Test
	@Order(6)
	public void updateNoteTest()
	{
		String username = "serdars";
		String password = "123456";

		String noteTitle = "Lorem ipsum!";
		String noteDescription = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr.";

		WebDriverWait wait = new WebDriverWait (driver, 30);
		driver.get("http://localhost:" + this.port + "/login");
		driver.manage().window().maximize();
		JavascriptExecutor jse =(JavascriptExecutor) driver;

		WebElement usernameElement = driver.findElement(By.id("inputUsername"));
		usernameElement.sendKeys(username);

		WebElement passwordElement = driver.findElement(By.id("inputPassword"));
		passwordElement.sendKeys(password);

		WebElement logInElement = driver.findElement(By.id("buttonLogIn"));
		wait.until(ExpectedConditions.elementToBeClickable(logInElement)).click();

		WebElement notesTab = driver.findElement(By.xpath("//a[@href='#nav-notes']"));
		jse.executeScript("arguments[0].click()", notesTab);

		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> noteList = notesTable.findElements(By.tagName("td"));

		boolean OK = false;

		for (int i = 0; i < noteList.size(); i++)
		{
			WebElement row = noteList.get(i);

			if (!row.isDisplayed())
			{
				jse.executeScript("arguments[0].scrollIntoView(true);", row);
			}

			WebElement editButton = null;

			try
			{
				editButton = row.findElement(By.className("editNote"));
			}
			catch (NoSuchElementException e)
			{
				continue;
			}

			if (!ObjectUtils.isEmpty(editButton))
			{
				wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();
				WebElement noteDescriptionElement = driver.findElement(By.id("note-edit-description"));
				wait.until(ExpectedConditions.elementToBeClickable(noteDescriptionElement)).click();
				noteDescriptionElement.clear();
				noteDescriptionElement.sendKeys(noteDescription);

				WebElement noteSubmitElement = driver.findElement(By.id("saveNoteEditChanges"));
				noteSubmitElement.click();
				Assertions.assertEquals("Home", driver.getTitle());
				break;
			}
		}

		jse.executeScript("arguments[0].click()", driver.findElement(By.xpath("//a[@href='#nav-notes']")));

		WebElement newNotesTable = driver.findElement(By.id("userTable"));
		List<WebElement> newNoteList = newNotesTable.findElements(By.tagName("td"));

		for (int j = 0; j < newNoteList.size(); j++)
		{
			WebElement newRow = newNoteList.get(j);

			if (!newRow.isDisplayed())
			{
				jse.executeScript("arguments[0].scrollIntoView(true);", newRow);
			}

			System.out.println(newRow.getAttribute("innerHTML") + " ::: " + noteDescription);

			if (newRow.getAttribute("innerHTML").equals(noteDescription))
			{
				OK = true;
				break;
			}
		}

		Assertions.assertTrue(OK);
	}

	@Test
	@Order(7)
	public void deleteNoteTest()
	{
		String username = "serdars";
		String password = "123456";

		WebDriverWait wait = new WebDriverWait (driver, 30);
		driver.get("http://localhost:" + this.port + "/login");
		driver.manage().window().maximize();
		JavascriptExecutor jse =(JavascriptExecutor) driver;

		WebElement usernameElement = driver.findElement(By.id("inputUsername"));
		usernameElement.sendKeys(username);

		WebElement passwordElement = driver.findElement(By.id("inputPassword"));
		passwordElement.sendKeys(password);

		WebElement logInElement = driver.findElement(By.id("buttonLogIn"));
		wait.until(ExpectedConditions.elementToBeClickable(logInElement)).click();

		WebElement notesTab = driver.findElement(By.xpath("//a[@href='#nav-notes']"));
		jse.executeScript("arguments[0].click()", notesTab);

		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> listOfNote = notesTable.findElements(By.tagName("a"));

		String id = null;

		for (int i = 0; i < listOfNote.size(); i++)
		{
			WebElement deleteButton = listOfNote.get(i);

			if (!ObjectUtils.isEmpty(deleteButton) && deleteButton.getAttribute("id").contains("note"))
			{
				id = deleteButton.getAttribute("id");
				wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
				break;
			}
		}

		jse.executeScript("arguments[0].click()", driver.findElement(By.xpath("//a[@href='#nav-notes']")));

		boolean OK = true;

		WebElement notesNew = driver.findElement(By.id("userTable"));
		List<WebElement> listOfNoteNew = notesNew.findElements(By.tagName("a"));

		for (int j = 0; j < listOfNoteNew.size(); j++)
		{
			WebElement newRow = listOfNoteNew.get(j);

			if (!newRow.isDisplayed())
			{
				jse.executeScript("arguments[0].scrollIntoView(true);", newRow);
			}

			if (!ObjectUtils.isEmpty(newRow) && newRow.getAttribute("id").contains("note") && newRow.getAttribute("id").equals(id))
			{
				OK = false;
				break;
			}
		}
		Assertions.assertTrue(OK);
	}

	@Test
	@Order(8)
	public void createCredentialTest()
	{
		String username = "serdars";
		String password = "123456";

		String url = "http://localhost:8080";
		String credentialUsername = "test";
		String credentialPassword = "test";

		WebDriverWait wait = new WebDriverWait (driver, 30);
		driver.get("http://localhost:" + this.port + "/login");
		driver.manage().window().maximize();
		JavascriptExecutor jse =(JavascriptExecutor) driver;

		WebElement usernameElement = driver.findElement(By.id("inputUsername"));
		usernameElement.sendKeys(username);

		WebElement passwordElement = driver.findElement(By.id("inputPassword"));
		passwordElement.sendKeys(password);

		WebElement logInElement = driver.findElement(By.id("buttonLogIn"));
		wait.until(ExpectedConditions.elementToBeClickable(logInElement)).click();

		WebElement notesTab = driver.findElement(By.xpath("//a[@href='#nav-credentials']"));
		jse.executeScript("arguments[0].click()", notesTab);

		WebElement addNewCredentialButton = driver.findElement(By.xpath("//button[@id='addNewCredential']"));
		wait.until(ExpectedConditions.elementToBeClickable(addNewCredentialButton)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url"))).sendKeys(url);;

		WebElement credentialUsernameElement = driver.findElement(By.id("credential-username"));
		credentialUsernameElement.click();
		credentialUsernameElement.sendKeys(credentialUsername);

		WebElement credentialPasswordElement = driver.findElement(By.id("credential-password"));
		credentialPasswordElement.click();
		credentialPasswordElement.sendKeys(credentialPassword);

		WebElement noteSubmit = driver.findElement(By.id("saveCredentialChanges"));
		noteSubmit.click();
		Assertions.assertEquals("Home", driver.getTitle());

		jse.executeScript("arguments[0].click()", driver.findElement(By.xpath("//a[@href='#nav-credentials']")));

		WebElement credentialTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> credentialList = credentialTable.findElements(By.tagName("td"));

		boolean OK = false;

		for (int i = 0; i < credentialList.size(); i++)
		{
			WebElement row = credentialList.get(i);

			if (!row.isDisplayed())
			{
				jse.executeScript("arguments[0].scrollIntoView(true);", row);
			}

			if (row.getAttribute("innerHTML").equals(credentialUsername))
			{
				OK = true;
				break;
			}
		}

		Assertions.assertTrue(OK);
	}

	@Test
	@Order(9)
	public void updateCredentialTest()
	{
		String username = "serdars";
		String password = "123456";

		String url = "http://localhost:8080";
		String credentialUsername = "test2";
		String credentialPassword = "test";

		WebDriverWait wait = new WebDriverWait (driver, 30);
		driver.get("http://localhost:" + this.port + "/login");
		driver.manage().window().maximize();
		JavascriptExecutor jse =(JavascriptExecutor) driver;

		WebElement usernameElement = driver.findElement(By.id("inputUsername"));
		usernameElement.sendKeys(username);

		WebElement passwordElement = driver.findElement(By.id("inputPassword"));
		passwordElement.sendKeys(password);

		WebElement logInElement = driver.findElement(By.id("buttonLogIn"));
		wait.until(ExpectedConditions.elementToBeClickable(logInElement)).click();

		WebElement credentialTab = driver.findElement(By.xpath("//a[@href='#nav-credentials']"));
		jse.executeScript("arguments[0].click()", credentialTab);

		WebElement credentialTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> credentialList = credentialTable.findElements(By.tagName("td"));

		boolean OK = false;

		for (int i = 0; i < credentialList.size(); i++)
		{
			WebElement row = credentialList.get(i);

			if (!row.isDisplayed())
			{
				jse.executeScript("arguments[0].scrollIntoView(true);", row);
			}

			WebElement editButton = null;

			try
			{
				editButton = row.findElement(By.className("editCredential"));
			}
			catch (NoSuchElementException e)
			{
				continue;
			}

			if (!ObjectUtils.isEmpty(editButton))
			{
				wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();
				WebElement credentialUsernameElement = driver.findElement(By.id("credential-edit-username"));
				wait.until(ExpectedConditions.elementToBeClickable(credentialUsernameElement)).click();
				credentialUsernameElement.clear();
				credentialUsernameElement.sendKeys(credentialUsername);

				WebElement credentialSubmitElement = driver.findElement(By.id("saveCredentialEditChanges"));
				credentialSubmitElement.click();
				Assertions.assertEquals("Home", driver.getTitle());
				break;
			}
		}

		jse.executeScript("arguments[0].click()", driver.findElement(By.xpath("//a[@href='#nav-credentials']")));

		WebElement newCredentialTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> newCredentialList = newCredentialTable.findElements(By.tagName("td"));

		for (int j = 0; j < newCredentialList.size(); j++)
		{
			WebElement newRow = newCredentialList.get(j);

			if (!newRow.isDisplayed())
			{
				jse.executeScript("arguments[0].scrollIntoView(true);", newRow);
			}

			System.out.println(newRow.getAttribute("innerHTML") + " ::: " + credentialUsername);

			if (newRow.getAttribute("innerHTML").equals(credentialUsername))
			{
				OK = true;
				break;
			}
		}

		Assertions.assertTrue(OK);
	}

	@Test
	@Order(10)
	public void deleteCredentialTest()
	{
		String username = "serdars";
		String password = "123456";

		WebDriverWait wait = new WebDriverWait (driver, 30);
		driver.get("http://localhost:" + this.port + "/login");
		driver.manage().window().maximize();
		JavascriptExecutor jse =(JavascriptExecutor) driver;

		WebElement usernameElement = driver.findElement(By.id("inputUsername"));
		usernameElement.sendKeys(username);

		WebElement passwordElement = driver.findElement(By.id("inputPassword"));
		passwordElement.sendKeys(password);

		WebElement logInElement = driver.findElement(By.id("buttonLogIn"));
		wait.until(ExpectedConditions.elementToBeClickable(logInElement)).click();

		WebElement credentialTab = driver.findElement(By.xpath("//a[@href='#nav-credentials']"));
		jse.executeScript("arguments[0].click()", credentialTab);

		WebElement credentialTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> listOfCredential = credentialTable.findElements(By.tagName("a"));

		String id = null;

		for (int i = 0; i < listOfCredential.size(); i++)
		{
			WebElement deleteButton = listOfCredential.get(i);

			if (!ObjectUtils.isEmpty(deleteButton) && deleteButton.getAttribute("id").contains("credential"))
			{
				id = deleteButton.getAttribute("id");
				wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
				break;
			}
		}

		jse.executeScript("arguments[0].click()", driver.findElement(By.xpath("//a[@href='#nav-credentials']")));

		boolean OK = true;

		WebElement credentialsNew = driver.findElement(By.id("credentialTable"));
		List<WebElement> listOfCredentialNew = credentialsNew.findElements(By.tagName("a"));

		for (int j = 0; j < listOfCredentialNew.size(); j++)
		{
			WebElement newRow = listOfCredentialNew.get(j);

			if (!newRow.isDisplayed())
			{
				jse.executeScript("arguments[0].scrollIntoView(true);", newRow);
			}

			if (!ObjectUtils.isEmpty(newRow) && newRow.getAttribute("id").contains("credential") && newRow.getAttribute("id").equals(id))
			{
				OK = false;
				break;
			}
		}
		Assertions.assertTrue(OK);
	}

}
