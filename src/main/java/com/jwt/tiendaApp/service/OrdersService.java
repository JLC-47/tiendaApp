package com.jwt.tiendaApp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jwt.tiendaApp.dtos.HttpGlobalResponseDTO;
import com.jwt.tiendaApp.dtos.OrderDTO.OrderResponseDTO;
import com.jwt.tiendaApp.entity.Orders;
import com.jwt.tiendaApp.entity.Products;
import com.jwt.tiendaApp.entity.Users;
import com.jwt.tiendaApp.enums.OrderStatusEnum;
import com.jwt.tiendaApp.repository.OrdersRepository;
import com.jwt.tiendaApp.repository.ProductsRepository;
import com.jwt.tiendaApp.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final ProductsRepository productsRepository;
    private final UsersRepository usersRepository;

    public HttpGlobalResponseDTO<OrderResponseDTO> createOrder(Long clientId, Long productId, Integer quantity) {
        HttpGlobalResponseDTO<OrderResponseDTO> response = new HttpGlobalResponseDTO<>();

        Optional<Users> client = usersRepository.findById(clientId);
        Optional<Products> productOpt = productsRepository.findById(productId);

        if (client.isEmpty() || productOpt.isEmpty()) {
            response.setMessage("Cliente o producto no encontrado (404)");
            response.setData(null);
            return response;
        }

        Products product = productOpt.get();

        if (product.getStock() < quantity) {
            response.setMessage("¡Pilaseeees!, no hay stock suficiente para este producto (400)");
            response.setData(null);
            return response;
        }

        product.setStock(product.getStock() - quantity);
        productsRepository.save(product);

        Double total = product.getPrice() * quantity;

        Orders order = new Orders();
        order.setClientId(clientId);
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setTotal(total);
        order.setStatus(OrderStatusEnum.PENDIENTE.name());

        Orders saved = ordersRepository.save(order);

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(saved.getId());
        dto.setClientId(saved.getClientId());
        dto.setProductId(saved.getProductId());
        dto.setQuantity(saved.getQuantity());
        dto.setTotal(saved.getTotal());
        dto.setStatus(saved.getStatus());
        dto.setCreatedAt(saved.getCreatedAt());

        response.setMessage("¡Pedido creado con éxito mi papacho! 📦");
        response.setData(dto);
        return response;
    }

    public HttpGlobalResponseDTO<List<OrderResponseDTO>> getOrdersByClient(Long clientId) {
        HttpGlobalResponseDTO<List<OrderResponseDTO>> response = new HttpGlobalResponseDTO<>();
        List<Orders> allOrders = ordersRepository.findAll();
        List<OrderResponseDTO> clientOrders = new ArrayList<>();

        for (Orders o : allOrders) {
            if (o.getClientId() != null && o.getClientId().equals(clientId)) {
                OrderResponseDTO dto = new OrderResponseDTO();
                dto.setId(o.getId());
                dto.setClientId(o.getClientId());
                dto.setProductId(o.getProductId());
                dto.setQuantity(o.getQuantity());
                dto.setTotal(o.getTotal());
                dto.setStatus(o.getStatus());
                dto.setCreatedAt(o.getCreatedAt());

                clientOrders.add(dto);
            }
        }

        response.setMessage("Tus pedidos realizados:");
        response.setData(clientOrders);
        return response;
    }

    public HttpGlobalResponseDTO<List<OrderResponseDTO>> getOrdersBySeller(Long sellerId) {
        HttpGlobalResponseDTO<List<OrderResponseDTO>> response = new HttpGlobalResponseDTO<>();
        List<Orders> allOrders = ordersRepository.findAll();
        List<OrderResponseDTO> sellerSales = new ArrayList<>();

        for (Orders o : allOrders) {
            Optional<Products> productOpt = productsRepository.findById(o.getProductId());
            if (productOpt.isPresent() && productOpt.get().getSellerId() != null &&
                productOpt.get().getSellerId().equals(sellerId)) {
                
                OrderResponseDTO dto = new OrderResponseDTO();
                dto.setId(o.getId());
                dto.setClientId(o.getClientId());
                dto.setProductId(o.getProductId());
                dto.setQuantity(o.getQuantity());
                dto.setTotal(o.getTotal());
                dto.setStatus(o.getStatus());
                dto.setCreatedAt(o.getCreatedAt());

                sellerSales.add(dto);
            }
        }

        response.setMessage("Ventas de tus productos:");
        response.setData(sellerSales);
        return response;
    }


    public HttpGlobalResponseDTO<List<OrderResponseDTO>> getAllOrders() {
        HttpGlobalResponseDTO<List<OrderResponseDTO>> response = new HttpGlobalResponseDTO<>();
        List<Orders> allOrders = ordersRepository.findAll();
        List<OrderResponseDTO> orderDTOList = new ArrayList<>();

        for (Orders o : allOrders) {
            OrderResponseDTO dto = new OrderResponseDTO();
            dto.setId(o.getId());
            dto.setClientId(o.getClientId());
            dto.setProductId(o.getProductId());
            dto.setQuantity(o.getQuantity());
            dto.setTotal(o.getTotal());
            dto.setStatus(o.getStatus());
            dto.setCreatedAt(o.getCreatedAt());

            orderDTOList.add(dto);
        }

        response.setMessage("Todos los pedidos del sistema (ADMIN):");
        response.setData(orderDTOList);
        return response;
    }


    public HttpGlobalResponseDTO<OrderResponseDTO> updateStatus(Long orderId, String status, Long currentUserId, Long currentUserRolId) {
        HttpGlobalResponseDTO<OrderResponseDTO> response = new HttpGlobalResponseDTO<>();
        Optional<Orders> orderOpt = ordersRepository.findById(orderId);

        if (orderOpt.isEmpty()) {
            response.setMessage("Pedido no encontrado (404)");
            response.setData(null);
            return response;
        }

        Orders order = orderOpt.get();
        if (currentUserRolId == 3) {
            Optional<Products> productOpt = productsRepository.findById(order.getProductId());
            if (productOpt.isPresent() && !productOpt.get().getSellerId().equals(currentUserId)) {
                response.setMessage("¡No puedes cambiar el estado de un pedido que no es tuyo! (403)");
                response.setData(null);
                return response;
            }
        }

        order.setStatus(status);
        Orders updated = ordersRepository.save(order);

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(updated.getId());
        dto.setClientId(updated.getClientId());
        dto.setProductId(updated.getProductId());
        dto.setQuantity(updated.getQuantity());
        dto.setTotal(updated.getTotal());
        dto.setStatus(updated.getStatus());
        dto.setCreatedAt(updated.getCreatedAt());

        response.setMessage("¡Estado del pedido actualizado!");
        response.setData(dto);
        return response;
    }

    public HttpGlobalResponseDTO<OrderResponseDTO> cancelOrder(Long orderId, Long clientId) {
        HttpGlobalResponseDTO<OrderResponseDTO> response = new HttpGlobalResponseDTO<>();
        Optional<Orders> orderOpt = ordersRepository.findById(orderId);

        if (orderOpt.isEmpty()) {
            response.setMessage("Pedido no encontrado (404)");
            response.setData(null);
            return response;
        }

        Orders order = orderOpt.get();
        if (!order.getClientId().equals(clientId)) {
            response.setMessage("Este pedido no te pertenece (403)");
            response.setData(null);
            return response;
        }

        if (!OrderStatusEnum.PENDIENTE.name().equalsIgnoreCase(order.getStatus())) {
            response.setMessage("Solo se pueden cancelar pedidos en estado PENDIENTE (400)");
            response.setData(null);
            return response;
        }

        Optional<Products> productOpt = productsRepository.findById(order.getProductId());
        if (productOpt.isPresent()) {
            Products product = productOpt.get();
            product.setStock(product.getStock() + order.getQuantity());
            productsRepository.save(product);
        }

        order.setStatus(OrderStatusEnum.CANCELADO.name());
        Orders cancelled = ordersRepository.save(order);

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(cancelled.getId());
        dto.setClientId(cancelled.getClientId());
        dto.setProductId(cancelled.getProductId());
        dto.setQuantity(cancelled.getQuantity());
        dto.setTotal(cancelled.getTotal());
        dto.setStatus(cancelled.getStatus());
        dto.setCreatedAt(cancelled.getCreatedAt());

        response.setMessage("¡Pedido cancelado y stock devuelto!");
        response.setData(dto);
        return response;
    }
    
}
