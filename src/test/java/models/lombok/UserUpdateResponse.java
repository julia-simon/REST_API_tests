package models.lombok;

import lombok.Data;

@Data
public class UserUpdateResponse {
    String name;
    String job;
    String updatedAt;
}
