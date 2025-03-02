package testbase;


import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

    public class ScenarioContext { //} extends Scenario {

        private Map<String, Object> scenarioContext;

        private WebDriver driver = null;

        public ScenarioContext(WebDriver _driver){
            //super();
            scenarioContext = new HashMap<String, Object>();
            driver = _driver; {
            }
        }

        public void setContext(String key, Object value) {
            scenarioContext.put(key, value);
        }

        public Object getContext(String key){
            return scenarioContext.get(key);
        }

        public Boolean isContains(String key){
            return scenarioContext.containsKey(key);
        }

        public WebDriver getDriver(){ return driver;}
    }
