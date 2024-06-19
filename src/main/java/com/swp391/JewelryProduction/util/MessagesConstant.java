package com.swp391.JewelryProduction.util;

import com.swp391.JewelryProduction.pojos.Account;
import com.swp391.JewelryProduction.pojos.Order;
import com.swp391.JewelryProduction.pojos.Staff;
import com.swp391.JewelryProduction.pojos.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessagesConstant {
    private String customerName;
    @Builder.Default
    private String companyName = "Dung";
    @Builder.Default
    private String companyContact = "jewelryshop.business@gmail.com";
    private String title;
    private String description;

    public class RequestApprovedMessage {
        public String title () {
            return title = "Your Request Has Been Approved";
        }

        public String description () {
            return description = String.format(
                    """
                    Dear %1$s,
    
                    I am pleased to inform you that your request for a custom jewelry has been approved. \
                    Our team has reviewed your submission and is pleased to confirm that your request meets the necessary \
                    criteria and requirements. As a result, we will be moving forward with the design phase. Please note \
                    that any further information or documentation required will be sent to you separately. If you have any \
                    questions or concerns, please do not hesitate to reach out to us at %2$s. Thank you \
                    for your patience and cooperation.
    
                    Best regards,
                    %3$s
                    """,
                    customerName, companyContact, companyName
            );
        }
    }

    public class RequestDeclinedMessage {
        public String title () {
            return title = "Your Request Has Been Declined";
        }

        public String description () {
            return description = String.format(
                    """
                    Dear %1$s,
                    
                    I regret to inform you that your request for a custom jewelry has been declined. \
                    After careful review, our team has determined that it does not align with our current \
                    priorities or requirements. We appreciate the effort you took to submit your request \
                    and apologize for any inconvenience this may cause. If you would like to resubmit your \
                    request or discuss alternative options, please do not hesitate to reach out to us at \
                    %2$s. We are always happy to consider alternative approaches and look forward to the \
                    possibility of working together in the future.
                    
                    Best regards,
                    %3$s
                    """,
                    customerName, companyContact, companyName
            );
        }
    }

    public class StaffAssignedMessage {
        public String title (UserInfo info) {
            return title = "Assignment to " + info.getFirstName() + " order request";
        }

        public String description (Staff assignedStaff, Order order, List<String> responsibilities) {
            StringBuilder builder = new StringBuilder();
            responsibilities.forEach(res -> builder.append(" - ").append(res).append(". \n"));
            LocalDateTime startDate = assignedStaff.getHistory().getLast().getStartDate();
            LocalDateTime endDate = startDate.plusMonths(1).plusWeeks(2);

            return description = String.format(
                    """
                    Dear %1$s,
                   
                    I am pleased to inform you that you have been assigned to the order id %2$s 
                    as one of the team members responsible for fulfilling the customer's order.
                    Your expertise and skills will be valuable assets in delivering a high-quality solution 
                    to our client.
                    
                    As part of the project team, your responsibilities will include:
                    
                    %3$s
                   
                    Your involvement in this project will commence on %4$s and is expected to be 
                    completed by %5$s. Please review the project scope and timeline carefully and 
                    ensure that you understand your role and responsibilities.
                    
                    If you have any questions or concerns, please do not hesitate to reach out to me or the 
                    project lead. I expect your full cooperation and commitment to delivering exceptional results.
                    
                    Congratulations on this assignment, and I look forward to seeing your contributions to 
                    the project's success.
                    
                    Best regards,
                    Manager
                    """,
                    assignedStaff.getUserInfo().getFirstName(), order.getId(),
                    builder.toString(), startDate, endDate
            );
        }
    }

    public class QuotationApprovedMessage {
        public String title (Account acc) {
            return title = "Quotation Approval Request for order id " + acc.getCurrentOrder().getId();
        }

        public String description () {
            return description = String.format(
                    """
                    Dear %1$s,
                    
                    I regret to inform you that your request for a custom jewelry has been declined. \
                    After careful review, our team has determined that it does not align with our current \
                    priorities or requirements. We appreciate the effort you took to submit your request \
                    and apologize for any inconvenience this may cause. If you would like to resubmit your \
                    request or discuss alternative options, please do not hesitate to reach out to us at \
                    %2$s. We are always happy to consider alternative approaches and look forward to the \
                    possibility of working together in the future.
                    
                    Best regards,
                    %3$s
                    """,
                    customerName, companyContact, companyName
            );
        }
    }

    public MessagesConstant createRequestApprovedMessage (String customerName) {
        if (customerName == null) return null;
        RequestApprovedMessage requestApprovedMessage = new RequestApprovedMessage();
        return MessagesConstant.builder()
                .title(requestApprovedMessage.title())
                .description(requestApprovedMessage.description())
                .build();
    }

    public MessagesConstant createRequestDeclinedMessage (String customerName) {
        if (customerName == null) return null;
        RequestDeclinedMessage requestDeclinedMessage = new RequestDeclinedMessage();
        return MessagesConstant.builder()
                .title(requestDeclinedMessage.title())
                .description(requestDeclinedMessage.description())
                .build();
    }

    public MessagesConstant createStaffAssignedMessage(Order order, Staff assignedStaff, List<String> responsibilities) {
        if (order == null || assignedStaff == null || responsibilities == null || responsibilities.isEmpty())
            return null;
        StaffAssignedMessage messageBuilder = new StaffAssignedMessage();
        return MessagesConstant.builder()
                .title(messageBuilder.title(order.getOwner().getUserInfo()))
                .description(messageBuilder.description(assignedStaff, order, responsibilities))
                .build();
    }
}
