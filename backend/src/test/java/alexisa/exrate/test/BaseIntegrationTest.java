package alexisa.exrate.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
@AutoConfigureTestEntityManager
@AutoConfigureMockMvc
@ActiveProfiles("int-test")
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private TestEntityManager testEntityManager;

    protected static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    protected void flushAndClear() {
        testEntityManager.flush();
        testEntityManager.clear();
    }
}
