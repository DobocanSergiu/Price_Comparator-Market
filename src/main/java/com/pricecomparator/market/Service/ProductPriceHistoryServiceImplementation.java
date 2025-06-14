package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.ProductPrice.*;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.DTO.Response.ProductPrice.ProductBestBuyResponse;
import com.pricecomparator.market.DTO.Response.ProductPrice.ProductDiscountResponse;
import com.pricecomparator.market.Domain.Product;
import com.pricecomparator.market.Domain.ProductPriceHistory;
import com.pricecomparator.market.Repository.ProductPriceHistoryRepository;
import com.pricecomparator.market.Repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ProductPriceHistoryServiceImplementation implements ProductPriceHistoryService{


    private final ProductRepository productRepository;
    private final ProductPriceHistoryRepository productPriceHistoryRepository;

    public ProductPriceHistoryServiceImplementation(ProductRepository productRepository, ProductPriceHistoryRepository productPriceHistoryRepository) {
        this.productRepository = productRepository;
        this.productPriceHistoryRepository = productPriceHistoryRepository;
    }


    ///Gets price of specific product during a specific date
    @Override
    public Optional<ProductPriceHistory> getProductPriceById(int productPriceId) {
        return productPriceHistoryRepository.findById(productPriceId);
    }

    /// Gets all prices of a specific product
    @Override
    public Optional<List<ProductPriceHistory>> getAllProductPrices(int productId) {
        Optional<Product> productWithId= productRepository.findById(productId);
        if(productWithId.isEmpty()){
            return Optional.empty();
        }
        Product foundProductWithId = productWithId.get();
        Optional<List<ProductPriceHistory>> productPriceList = productPriceHistoryRepository.getAllByProductid(foundProductWithId);
        return productPriceList;
    }

    /// Remove specific price from a product
    @Override
    public HttpCode removeProductPriceById(int productPriceId) {
        if(productPriceHistoryRepository.existsById(productPriceId)){
            productPriceHistoryRepository.deleteById(productPriceId);
            HttpCode response = new HttpCode();
            response.setCode(204);
            response.setMessage("Product price deleted");
            return response;
        }
        else
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product price not found");
            return response;
        }
    }

    /// Removes all prices from a specific product
    @Override
    @Transactional
    public HttpCode clearPriceHistoryOfProductById(int productId) {
        Optional<Product> productOpt = productRepository.findById(productId);

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            productPriceHistoryRepository.deleteAllByProductid(product); // Ensure correct method name
            HttpCode response = new HttpCode();
            response.setCode(204);
            response.setMessage("Product price cleared");
            return response;
        } else {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product id not found");
            return response;
        }
    }

    /// Adds new product price
    @Override
    public HttpCode addProductPrice(CreateProductPriceRequest request) {
        int product_id = request.getProductId();
        String currency = request.getCurrency();
        String request_date = request.getDate();
        String price  = request.getPrice();
        int percentage = request.getPercentage();

        /// Product must exist
        if(productRepository.existsById(product_id)==false)
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product id not found");
            return response;

        }
        /// Get product with specific productId
        Product product = productRepository.findById(product_id).get();
        /// Transform initial date represented as string into Instant data type
        Instant date = LocalDate.parse(request_date).atStartOfDay(ZoneId.systemDefault()).toInstant().plus(1, ChronoUnit.DAYS);


        /// entry with the same product id and price must not exist
        List<ProductPriceHistory> productsWithSpecificProductIdAndDate  = productPriceHistoryRepository.findByProductidAndDate(product,date);
        if(!productsWithSpecificProductIdAndDate.isEmpty())
        {
                HttpCode response = new HttpCode();
                response.setCode(409);
                response.setMessage("Product price at specific date already exists");
                return response;
        }

        /// After validation create and add entry
        ProductPriceHistory entry = new ProductPriceHistory();
        entry.setProductid(product);
        entry.setCurrency(currency);
        entry.setPrice(new BigDecimal(price));
        entry.setDate(date);
        entry.setPricedecreasepercentage(BigDecimal.valueOf(percentage));
        productPriceHistoryRepository.save(entry);
        HttpCode response = new HttpCode();
        response.setCode(204);
        response.setMessage("Product price added");
        return response;



    }

    /// Creates sale period meaning multiple entries for a specific product at a specific price across multiple days
    @Override
    public HttpCode addSalePeriod(CreateSaleRequest request) {

        String currency = request.getCurrency();
        String price = request.getPrice();
        int product_id = request.getProductId();
        int percentage = request.getPercentage();
        String start_date = request.getStartDate();
        String end_date = request.getEndDate();

        if(productRepository.existsById(product_id)==false)
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product id not found");
            return response;

        }

        /// Get product with specific productId
        Product product = productRepository.findById(product_id).get();
        /// Transform initial date represented as string into Instant data type
        Instant startDate = LocalDate.parse(start_date).atStartOfDay(ZoneId.systemDefault()).toInstant().plus(1, ChronoUnit.DAYS);
        Instant endDate = LocalDate.parse(end_date).atStartOfDay(ZoneId.systemDefault()).toInstant().plus(1, ChronoUnit.DAYS);

        Instant currentDate = startDate;
        while(!currentDate.isAfter(endDate))
        {
            /// Going through each date in range to check if it is already used
            List<ProductPriceHistory> productsWithSpecificProductIdAndDate  = productPriceHistoryRepository.findByProductidAndDate(product,currentDate);
            if(!productsWithSpecificProductIdAndDate.isEmpty())
            {
                HttpCode response = new HttpCode();
                response.setCode(409);
                response.setMessage("Product price at specific date already exists");
                return response;
            }
            currentDate= currentDate.plus(1, ChronoUnit.DAYS);

        }

        /// After validation, create all entries from range and add to database
        currentDate = startDate;
        while(!currentDate.isAfter(endDate))
        {
            ProductPriceHistory entry = new ProductPriceHistory();
            entry.setProductid(product);
            entry.setCurrency(currency);
            entry.setPrice(new BigDecimal(price));
            entry.setDate(currentDate);
            entry.setPricedecreasepercentage(BigDecimal.valueOf(percentage));
            productPriceHistoryRepository.save(entry);


            currentDate= currentDate.plus(1, ChronoUnit.DAYS);


        }
        HttpCode response = new HttpCode();
        response.setCode(204);
        response.setMessage("Product prices added");
        return response;


    }

    /// Updates currency for all prices of specific product
    @Override
    public HttpCode updateCurrency(UpdateProductPriceCurencyRequest request) {

        int product_id = request.getProductId();
        String currency = request.getCurrency();

        /// Check to see if product exists
        if(productRepository.existsById(product_id)==false)
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product with given id does not exist");
            return response;
        }

        Optional<List<ProductPriceHistory>> productsWithGivenProductId =productPriceHistoryRepository.getAllByProductid(productRepository.findById(product_id).get());
        if(!productsWithGivenProductId.isPresent() || productsWithGivenProductId.isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product prices for given id not found");
            return response;
        }

        /// Update currency of all entries
        for(int i =0;i<productsWithGivenProductId.get().size();i++)
        {
            ProductPriceHistory currentProductPrice = productsWithGivenProductId.get().get(i);
            currentProductPrice.setCurrency(currency);
            productPriceHistoryRepository.save(currentProductPrice);

        }
        HttpCode response = new HttpCode();
        response.setCode(204);
        response.setMessage("Product prices updated");
        return response;
    }

    /// Updates date of a specific product at a specific price
    @Override
    public HttpCode updateDate(UpdateProductPriceDateRequest request) {

        String date = request.getDate();
        int productPriceId = request.getProductPriceId();

        /// Checks to see if specific product price exists
        if(productPriceHistoryRepository.existsById(productPriceId)==false)
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product price with given id does not exist");
            return response;
        }
        ProductPriceHistory productPrice = productPriceHistoryRepository.findById(productPriceId).get();
        productPrice.setDate(LocalDate.parse(date).atStartOfDay(ZoneId.systemDefault()).toInstant().plus(1, ChronoUnit.DAYS));
        productPriceHistoryRepository.save(productPrice);
        HttpCode response = new HttpCode();
        response.setCode(200);
        response.setMessage("Product price updated");
        return response;

    }

    /// Updates monetary value of a specific price; Does not update currency type (Ex: from 10 USD to 20 USD)
    @Override
    public HttpCode updatePrice(UpdateProductPriceRequest request) {
        String price = request.getPrice();
        int productPriceId = request.getProductPriceId();
        if(productPriceHistoryRepository.existsById(productPriceId)==false)
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product price with given id does not exist");
            return response;
        }
        ProductPriceHistory productPrice = productPriceHistoryRepository.findById(productPriceId).get();
        productPrice.setPrice(new BigDecimal(price));
        productPriceHistoryRepository.save(productPrice);
        HttpCode response = new HttpCode();
        response.setCode(200);
        response.setMessage("Product price updated");
        return response;

    }

    /// Updates price decrease percentage
    @Override
    public HttpCode updateProductPriceDecreasePercentage(UpdateProductPriceDecreasePercentageRequest request) {
        int percentage = request.getPercentage();
        int productPriceId = request.getProductPriceId();
        if(productPriceHistoryRepository.existsById(productPriceId)==false)
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product price with given id does not exist");
            return response;
        }
        ProductPriceHistory productPrice = productPriceHistoryRepository.findById(productPriceId).get();
        productPrice.setPricedecreasepercentage(new BigDecimal(percentage));
        productPriceHistoryRepository.save(productPrice);
        HttpCode response = new HttpCode();
        response.setCode(200);
        response.setMessage("Product price updated");
        return response;

    }



    /// New Functions
    @Override
    public List<ProductDiscountResponse> getAllDiscounts()
    {
        List<ProductPriceHistory> productPriceHistory = productPriceHistoryRepository.findAll();
        List<ProductDiscountResponse> output = new ArrayList<ProductDiscountResponse>();

        /// Finds all entries that have a price decrease applied
        for(int i =0;i<productPriceHistory.size();i++)
        {
            ProductPriceHistory currentProductPrice  = productPriceHistory.get(i);
            BigDecimal currentProductPriceDecrease = currentProductPrice.getPricedecreasepercentage();
            Product currentProduct = productRepository.findById(currentProductPrice.getProductid().getId()).get();
            if(currentProductPriceDecrease.compareTo(BigDecimal.ZERO) == 1)
            {

                ProductDiscountResponse current = new ProductDiscountResponse(currentProductPrice);
                current.setProductName(currentProduct.getName());
                current.setProductBrand(currentProduct.getBrand());
                current.setStore(currentProduct.getStore());
                output.add(current);


            }
        }
        /// Sort based on percentage decrease. Highest decrease first.
        output.sort((p1,p2)->p2.getPriceDecreasePercentage().compareTo(p1.getPriceDecreasePercentage()));
        return output;

    }

    @Override
    public List<ProductDiscountResponse> getAllPresentOrFutureDiscounts()
    {
        List<ProductPriceHistory> productPriceHistory = productPriceHistoryRepository.findAll();
        List<ProductDiscountResponse> output = new ArrayList<ProductDiscountResponse>();

        /// Finds all entries that have a price decrease applied
        for(int i =0;i<productPriceHistory.size();i++)
        {
            ProductPriceHistory currentProductPrice  = productPriceHistory.get(i);
            BigDecimal currentProductPriceDecrease = currentProductPrice.getPricedecreasepercentage();
            Product currentProduct = productRepository.findById(currentProductPrice.getProductid().getId()).get();
            if(currentProductPriceDecrease.compareTo(BigDecimal.ZERO) == 1)
            {

                ProductDiscountResponse current = new ProductDiscountResponse(currentProductPrice);
                current.setProductName(currentProduct.getName());
                current.setProductBrand(currentProduct.getBrand());
                current.setStore(currentProduct.getStore());
                output.add(current);


            }
        }
        /// Remove past discounts
        Instant now = Instant.now();
        output.removeIf(x->{
            Instant currentDate =Instant.parse(x.getDate());
            return currentDate.isBefore(now);}
        );
        /// Sort based on percentage decrease. Highest decrease first.
        output.sort((p1,p2)->p2.getPriceDecreasePercentage().compareTo(p1.getPriceDecreasePercentage()));
        return output;

    }

    @Override
    public List<ProductDiscountResponse> getAllDiscountsByStore(String store) {
        List<ProductPriceHistory> productPriceHistory = productPriceHistoryRepository.findAll();
        List<ProductDiscountResponse> output = new ArrayList<ProductDiscountResponse>();

        /// Finds all entries that have a price decrease applied
        for(int i =0;i<productPriceHistory.size();i++)
        {
            ProductPriceHistory currentProductPrice  = productPriceHistory.get(i);
            BigDecimal currentProductPriceDecrease = currentProductPrice.getPricedecreasepercentage();
            Product currentProduct = productRepository.findById(currentProductPrice.getProductid().getId()).get();
            if(currentProductPriceDecrease.compareTo(BigDecimal.ZERO) == 1)
            {

                ProductDiscountResponse current = new ProductDiscountResponse(currentProductPrice);
                current.setProductName(currentProduct.getName());
                current.setProductBrand(currentProduct.getBrand());
                current.setStore(currentProduct.getStore());
                output.add(current);


            }
        }
        /// Sort based on percentage decrease. Highest decrease first.
        output.sort((p1,p2)->p2.getPriceDecreasePercentage().compareTo(p1.getPriceDecreasePercentage()));
        output.removeIf(x->x.getStore().equals(store)==false);
        return output;
    }

    @Override
    public List<ProductDiscountResponse> getAllPresentOrFutureDiscountsByStore(String store) {
        List<ProductPriceHistory> productPriceHistory = productPriceHistoryRepository.findAll();
        List<ProductDiscountResponse> output = new ArrayList<ProductDiscountResponse>();

        /// Finds all entries that have a price decrease applied
        for(int i =0;i<productPriceHistory.size();i++)
        {
            ProductPriceHistory currentProductPrice  = productPriceHistory.get(i);
            BigDecimal currentProductPriceDecrease = currentProductPrice.getPricedecreasepercentage();
            Product currentProduct = productRepository.findById(currentProductPrice.getProductid().getId()).get();
            if(currentProductPriceDecrease.compareTo(BigDecimal.ZERO) == 1)
            {

                ProductDiscountResponse current = new ProductDiscountResponse(currentProductPrice);
                current.setProductName(currentProduct.getName());
                current.setProductBrand(currentProduct.getBrand());
                current.setStore(currentProduct.getStore());
                output.add(current);


            }
        }
        /// Remove past discounts
        Instant now = Instant.now();
        output.removeIf(x->{
            Instant currentDate =Instant.parse(x.getDate());
            return currentDate.isBefore(now);}
        );
        /// Sort based on percentage decrease. Highest decrease first.
        output.sort((p1,p2)->p2.getPriceDecreasePercentage().compareTo(p1.getPriceDecreasePercentage()));
        output.removeIf(x->x.getStore().equals(store)==false);
        return output;
    }

    @Override
    public List<ProductDiscountResponse> getLast24hDiscounts()
    {
        List<ProductPriceHistory> productPriceHistory = productPriceHistoryRepository.findAll();
        List<ProductDiscountResponse> output = new ArrayList<ProductDiscountResponse>();

        /// Finds all entries that have a price decrease applied
        for(int i =0;i<productPriceHistory.size();i++)
        {
            ProductPriceHistory currentProductPrice  = productPriceHistory.get(i);
            BigDecimal currentProductPriceDecrease = currentProductPrice.getPricedecreasepercentage();
            Product currentProduct = productRepository.findById(currentProductPrice.getProductid().getId()).get();
            if(currentProductPriceDecrease.compareTo(BigDecimal.ZERO) == 1)
            {

                ProductDiscountResponse current = new ProductDiscountResponse(currentProductPrice);
                current.setProductName(currentProduct.getName());
                current.setProductBrand(currentProduct.getBrand());
                current.setStore(currentProduct.getStore());
                output.add(current);


            }
        }
        /// Remove All Elements that arent in the previous 24 hours time period
        /// Coordinated Universal Time is 3 hours behind Romania time
        Instant now = Instant.now().plus(3, ChronoUnit.HOURS);

        System.out.println(now);
        output.removeIf(x->{
            Instant currentDate =Instant.parse(x.getDate());
            return !(currentDate.isBefore(now) &&currentDate.isAfter(now.minus(24,ChronoUnit.HOURS))) ;}
        );
        /// Sort based on percentage decrease. Highest decrease first.
        output.sort((p1,p2)->p2.getPriceDecreasePercentage().compareTo(p1.getPriceDecreasePercentage()));
        return output;

    }

    @Override
    public List<List<ProductDiscountResponse>> getPricesOfGivenProductAtStores(String productName) {
        List<String> storeNameList = new ArrayList<String>();
        List<Integer> productId = new ArrayList<Integer>();
        List<Product> productList = productRepository.findAllByName(productName);
        List<List<ProductDiscountResponse>> output = new ArrayList<>();

        /// Get all product Id's and store name of specified product
        for(int i =0;i<productList.size();i++)
        {
            Product product = productList.get(i);
            storeNameList.add(product.getStore());
            productId.add(product.getId());
        }

        /// For each store, make a list of prices for specified product
        for(int i =0;i<storeNameList.size();i++)
        {
            Product currentProduct = productRepository.findById(productId.get(i)).get();
            List<ProductPriceHistory> productPriceAtStore=productPriceHistoryRepository.getAllByProductid(currentProduct).get();
            List<ProductDiscountResponse> currentStoreList = new ArrayList<>();
            for(int j =0;j<productPriceAtStore.size();j++)
            {
                ProductPriceHistory currentProductPriceAtStore = productPriceAtStore.get(j);
                ProductDiscountResponse current = new ProductDiscountResponse(currentProductPriceAtStore);
                current.setProductName(currentProduct.getName());
                current.setProductBrand(currentProduct.getBrand());
                current.setStore(currentProduct.getStore());
                currentStoreList.add(current);
            }
            output.add(currentStoreList);
        }

        return output;

    }

    @Override
    public List<ProductDiscountResponse> getPricesOfGivenProductAtSpecificStore(String productName, String store) {
        Product product = productRepository.findByNameAndStore(productName,store);
        if(product==null)
        {
            return Collections.emptyList();
        }
        List<ProductPriceHistory> productPriceAtStore=productPriceHistoryRepository.getAllByProductid(product).get();
        List<ProductDiscountResponse> storeList = new ArrayList<>();
        for(int j =0;j<productPriceAtStore.size();j++)
        {
            ProductPriceHistory currentProductPriceAtStore = productPriceAtStore.get(j);
            ProductDiscountResponse current = new ProductDiscountResponse(currentProductPriceAtStore);
            current.setProductName(product.getName());
            current.setProductBrand(product.getBrand());
            current.setStore(product.getStore());
            storeList.add(current);
        }

        return storeList;


    }

    public BigDecimal getCurrentPriceByProductId(int productId) {
        // Fetch the product by ID
        Product wantedIdProduct = productRepository.findById(productId).orElse(null);
        if (wantedIdProduct == null) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }

        // Fetch all price history entries for the product
        List<ProductPriceHistory> productPricesList = productPriceHistoryRepository.getAllByProductid(wantedIdProduct).orElse(new ArrayList<>());

        if (productPricesList.isEmpty()) {
            return BigDecimal.ZERO; // or throw an exception depending on your logic
        }

        Instant today = Instant.now();
        ProductPriceHistory closestPrice = null;

        for (ProductPriceHistory priceHistory : productPricesList) {
            Instant priceDate = priceHistory.getDate();

            if (priceDate.isAfter(today)) {
                continue; // Skip future prices
            }

            // If it's exactly today, return it immediately
            if (priceDate.truncatedTo(ChronoUnit.DAYS).equals(today.truncatedTo(ChronoUnit.DAYS))) {
                return priceHistory.getPrice();
            }

            // Keep track of the most recent price before today
            if (closestPrice == null || priceDate.isAfter(closestPrice.getDate())) {
                closestPrice = priceHistory;
            }
        }

        // Return the closest price from the past if found, otherwise 0
        if (closestPrice != null) {
            return closestPrice.getPrice();
        }
        else {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public ProductBestBuyResponse getBestBuy(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name must not be null or empty.");
        }

        List<Product> productList = productRepository.findAllByName(productName);

        if (productList == null || productList.isEmpty()) {
            throw new RuntimeException("No products found with name: " + productName);
        }

        BigDecimal bestValue = null;
        Product bestProduct = null;

        for (Product product : productList) {
            BigDecimal currentPrice = getCurrentPriceByProductId(product.getId());
            String measurement = product.getMeasurement();
            double quantity = product.getQuantity();

            if ("ml".equalsIgnoreCase(measurement) || "g".equalsIgnoreCase(measurement)) {
                quantity = quantity / 1000.0;
            }

            if (quantity <= 0) {
                continue; // avoid division by zero or nonsense data
            }

            BigDecimal quantityBD = BigDecimal.valueOf(quantity);
            BigDecimal currentValue = currentPrice.divide(quantityBD, 4, RoundingMode.HALF_UP);

            if (bestValue == null || currentValue.compareTo(bestValue) < 0) {
                bestValue = currentValue;
                bestProduct = product;
            }
        }

        if (bestProduct == null) {
            throw new RuntimeException("Could not determine best buy for product: " + productName);
        }

        BigDecimal finalPrice = getCurrentPriceByProductId(bestProduct.getId());

        Optional<ProductPriceHistory> priceHistoryOpt = productPriceHistoryRepository.findByProductid(bestProduct);

        String currency = priceHistoryOpt
                .map(ProductPriceHistory::getCurrency)
                .orElse("UNKNOWN"); // or throw custom error
        ProductBestBuyResponse output = new ProductBestBuyResponse();
        output.setProductId(bestProduct.getId());
        output.setProductName(productName);
        output.setBrandName(bestProduct.getBrand());
        output.setPrice(finalPrice);
        output.setCurrency(currency);
        output.setStore(bestProduct.getStore());

        return output;
    }

}
