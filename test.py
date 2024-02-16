from selenium import webdriver
from selenium.webdriver.common.by import By

driver = webdriver.Chrome()

driver.get("https://cas.univ-avignon.fr/cas/login?service=https%3A%2F%2Fedt.univ-avignon.fr%2Flogin")

title = driver.title

input_username = driver.find_element(by=By.ID, value="username")
input_username = driver.find_element(by=By.ID, value="password")



print(title)

driver.quit()