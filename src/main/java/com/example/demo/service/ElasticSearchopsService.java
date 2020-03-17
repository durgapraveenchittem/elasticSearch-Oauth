package com.example.demo.service;

import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Employee;
import com.example.demo.view.AggregationsView;

@Service
@Transactional
public class ElasticSearchopsService {

	@Autowired
	private ElasticsearchTemplate template;

	public List<Employee> ageRange(int from, int to) {
		RangeQueryBuilder query = QueryBuilders.rangeQuery("age").gte(from).lte(to);
		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(query).build();
		List<Employee> employees = template.queryForList(searchQuery, Employee.class);
		return employees;
	}

	public List<Employee> salRange(int from, int to) {

		RangeQueryBuilder query = QueryBuilders.rangeQuery("sal").gte(from).lte(to);
		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(query).build();
		List<Employee> employees = template.queryForList(searchQuery, Employee.class);
		return employees;
	}

	public List<Employee> serachMultiField(String name, int age) {
		QueryBuilder query = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("name", name))
				.must(QueryBuilders.matchQuery("age", age));
		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(query).build();
		List<Employee> employees = template.queryForList(searchQuery, Employee.class);
		return employees;
	}

	public List<Employee> customSearch(String input) {
		String name = ".*" + input + ".*";
		NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
				.withFilter(QueryBuilders.regexpQuery("name", name)).build();
		List<Employee> employees = template.queryForList(nativeSearchQuery, Employee.class);
		return employees;
	}

	public List<Employee> search(String text) {
		NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders
				.multiMatchQuery(text).field("name").field("dept").type(MultiMatchQueryBuilder.Type.BEST_FIELDS))
				.build();
		List<Employee> employees = template.queryForList(nativeSearchQuery, Employee.class);
		return employees;
	}

	public String minAge() {
		SearchResponse response = template.getClient().prepareSearch().setQuery(QueryBuilders.matchAllQuery())
				.addAggregation(AggregationBuilders.min("agg").field("age")).execute().actionGet();

		Min agg = response.getAggregations().get("agg");

		double age = agg.getValue();

		return "MinAge:" + age;
	}

	public String maxsal() {
		SearchResponse response = template.getClient().prepareSearch().setQuery(QueryBuilders.matchAllQuery())
				.addAggregation(AggregationBuilders.max("maxsal").field("sal")).execute().actionGet();
		Max sal = response.getAggregations().get("maxsal");
		double maxsal = sal.getValue();
		return "MaxSal:" + maxsal;
	}

	public AggregationsView allAggregations() {
		SearchResponse response = template.getClient().prepareSearch().setQuery(QueryBuilders.matchAllQuery())
				.addAggregation(AggregationBuilders.min("minAge").field("age"))
				.addAggregation(AggregationBuilders.max("maxAge").field("age"))
				.addAggregation(AggregationBuilders.avg("avgAge").field("age"))
				.addAggregation(AggregationBuilders.min("minSal").field("sal"))
				.addAggregation(AggregationBuilders.max("maxSal").field("sal"))
				.addAggregation(AggregationBuilders.sum("sumSal").field("sal"))
				.addAggregation(AggregationBuilders.avg("avgSal").field("sal")).execute().actionGet();

		Min minAge1 = response.getAggregations().get("minAge");
		Min minSal1 = response.getAggregations().get("minSal");
		Max maxAge1 = response.getAggregations().get("maxAge");
		Max maxSal1 = response.getAggregations().get("maxSal");
		Avg avgAge1 = response.getAggregations().get("avgAge");
		Avg avgSal1 = response.getAggregations().get("avgSal");
		Sum sumSal1 = response.getAggregations().get("sumSal");

		double minAge = minAge1.getValue();
		double maxAge = maxAge1.getValue();
		double minSal = minSal1.getValue();
		double maxSal = maxSal1.getValue();
		double avgAge = avgAge1.getValue();
		double avgSal = avgSal1.getValue();
		double sumSal = sumSal1.getValue();

		AggregationsView aggregationsView = new AggregationsView();
		aggregationsView.setAvgAge(avgAge);
		aggregationsView.setAvgSal(avgSal);
		aggregationsView.setMinAge(minAge);
		aggregationsView.setMinSal(minSal);
		aggregationsView.setMaxAge(maxAge);
		aggregationsView.setMaxSal(maxSal);
		aggregationsView.setSumSal(sumSal);

		return aggregationsView;
	}

}
