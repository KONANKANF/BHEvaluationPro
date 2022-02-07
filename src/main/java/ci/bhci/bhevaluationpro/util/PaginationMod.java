package ci.bhci.bhevaluationpro.util;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import lombok.Data;

/**
 * Pagination Mod generic class object. Page module to talk with service that we create
 * @author kyao
 *
 * @param <T>
 */

@Data
public class PaginationMod<T> {
	private int number;
	private int size;
	private Sort sort;
	private int totalPages;
	private Long totalElements;
	private List<T> content; // data that returned

	/**
	 * Calling the setvalue method creates an instance of the paginationModule
	 * object filled our own object through Spring's Page
	 * 
	 * @param page
	 * @param list
	 */
	public void setValue(Page<T> page, List<T> list) {
		this.number = page.getNumber();
		this.size = page.getSize();
		this.sort = page.getSort();
		this.totalPages = page.getTotalPages();
		this.totalElements = page.getTotalElements();
		this.content = list;
	}
}