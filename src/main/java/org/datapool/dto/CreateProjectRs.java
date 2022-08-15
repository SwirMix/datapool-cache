package org.datapool.dto;

public class CreateProjectRs {
    private String name;
    private String description;
    private Result result;
    private ErrorMessage errorMessage;

    public String getName() {
        return name;
    }

    public CreateProjectRs setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CreateProjectRs setDescription(String description) {
        this.description = description;
        return this;
    }

    public Result getResult() {
        return result;
    }

    public CreateProjectRs setResult(Result result) {
        this.result = result;
        return this;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public CreateProjectRs setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }
}
