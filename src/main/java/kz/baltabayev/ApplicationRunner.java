package kz.baltabayev;

import kz.baltabayev.controller.MainController;
import kz.baltabayev.in.InputData;
import kz.baltabayev.out.OutputData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationRunner {

    private static MainController controller;
    private static UserStage userStage;

    public static void run() {
        ApplicationContext.loadContext();
        InputData inputData = (InputData) ApplicationContext.getBean("input");
        OutputData outputData = (OutputData) ApplicationContext.getBean("output");
        controller = (MainController) ApplicationContext.getBean("controller");
        userStage = UserStage.SECURITY;
        outputData.output("Welcome!");

        boolean processIsRun = true;
        while (processIsRun) {
            try {
                switch (userStage) {
                    case SECURITY -> securityProcess(inputData, outputData);

                    case EXIT -> {
                        exitProcess(outputData);
                        processIsRun = false;
                    }
                }
            } catch (RuntimeException e) {

            }
        }
        inputData.closeInput();

    }


    enum UserStage {
        SECURITY,
        EXIT
    }
}
