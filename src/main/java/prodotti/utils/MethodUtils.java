package prodotti.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class MethodUtils {
    public static Pageable getPagination(final Integer offset, final Integer limit){
        return PageRequest.of(offset != null ? offset:0, limit!=null ? limit:10);
    }
}
