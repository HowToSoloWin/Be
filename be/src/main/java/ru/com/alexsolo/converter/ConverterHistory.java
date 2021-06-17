package ru.com.alexsolo.converter;

import org.springframework.stereotype.Component;
import ru.com.alexsolo.Dto.HistoryDto;
import ru.com.alexsolo.domain.History;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConverterHistory {

    public List<HistoryDto> converter(List<History> historyList){
        List<HistoryDto> historyDto = new ArrayList<>();
        historyList.forEach(history -> historyDto.add(new HistoryDto(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(history.getDate()),
                history.getUser().getFirstName(),history.getAction(),history.getDescription())));

        return historyDto;
    }
}
