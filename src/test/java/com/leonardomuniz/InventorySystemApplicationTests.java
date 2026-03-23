package com.leonardomuniz;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InventorySystemApplicationTests {

    @Test
    void contextLoads() {
        // Este teste continua aqui para garantir que o contexto do Spring sobe sem erros
    }

    @Test
    void mainMethodTest() {
        // Invoca o método main explicitamente para cobrir a linha no JaCoCo
        InventorySystemApplication.main(new String[]{});
    }

}